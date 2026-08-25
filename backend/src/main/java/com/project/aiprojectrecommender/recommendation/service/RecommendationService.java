package com.project.aiprojectrecommender.recommendation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.aiprojectrecommender.entity.ActiveProject;
import com.project.aiprojectrecommender.entity.ProjectRecommendation;
import com.project.aiprojectrecommender.entity.User;
import com.project.aiprojectrecommender.entity.UserProfile;
import com.project.aiprojectrecommender.entity.UserSkill;
import com.project.aiprojectrecommender.llm.service.GeminiService;
import com.project.aiprojectrecommender.llm.util.PromptBuilder;
import com.project.aiprojectrecommender.recommendation.dto.RecommendationResponse;
import com.project.aiprojectrecommender.repository.ActiveProjectRepository;
import com.project.aiprojectrecommender.repository.ProjectRecommendationRepository;
import com.project.aiprojectrecommender.repository.UserProfileRepository;
import com.project.aiprojectrecommender.repository.UserRepository;
import com.project.aiprojectrecommender.repository.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserSkillRepository userSkillRepository;
    private final ProjectRecommendationRepository projectRecommendationRepository;
    private final ActiveProjectRepository activeProjectRepository;

    private final PromptBuilder promptBuilder;
    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;

    @Transactional
    public RecommendationResponse recommendProjects(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found.")
                );

        // ---------------------------------------------------------
        // STEP 1: Check whether recommendations already exist
        // ---------------------------------------------------------

        List<ProjectRecommendation> savedRecommendations =
                projectRecommendationRepository
                        .findByUserOrderByIdAsc(user);

        if (!savedRecommendations.isEmpty()) {

            return buildResponseFromDatabase(
                    savedRecommendations
            );
        }

        // ---------------------------------------------------------
        // STEP 2: No saved recommendations -> call Gemini
        // ---------------------------------------------------------

        UserProfile profile =
                userProfileRepository.findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException("Profile not found.")
                        );

        List<UserSkill> userSkills =
                userSkillRepository.findByUser(user);

        String prompt =
                promptBuilder.buildPrompt(
                        profile,
                        userSkills
                );

        String geminiOutput =
                geminiService.generate(prompt);

        // ---------------------------------------------------------
        // STEP 3: Parse Gemini response
        // ---------------------------------------------------------

        try {

            RecommendationResponse response =
                    objectMapper.readValue(
                            geminiOutput,
                            RecommendationResponse.class
                    );

            // -----------------------------------------------------
            // STEP 4: Save recommendations to PostgreSQL
            // -----------------------------------------------------

            saveRecommendations(user, response);

            return response;

        } catch (JsonProcessingException e) {

            throw new RuntimeException(
                    "Failed to parse Gemini recommendation response.",
                    e
            );
        }
    }

    // -------------------------------------------------------------
    // SAVE RECOMMENDATIONS
    // -------------------------------------------------------------

    private void saveRecommendations(
            User user,
            RecommendationResponse response) {

        if (response.getProjects() == null) {
            return;
        }

        for (
                RecommendationResponse.ProjectRecommendation recommendation
                : response.getProjects()
        ) {

            try {

                String recommendationJson =
                        objectMapper.writeValueAsString(
                                recommendation
                        );

                ProjectRecommendation entity =
                        ProjectRecommendation.builder()
                                .user(user)
                                .title(recommendation.getTitle())
                                .recommendationData(recommendationJson)
                                .createdAt(LocalDateTime.now())
                                .build();

                entity =
                        projectRecommendationRepository.save(entity);

                recommendation.setId(entity.getId());

            } catch (JsonProcessingException e) {

                throw new RuntimeException(
                        "Failed to save project recommendation.",
                        e
                );
            }
        }
    }

    // -------------------------------------------------------------
    // BUILD RESPONSE FROM DATABASE
    // -------------------------------------------------------------

    private RecommendationResponse buildResponseFromDatabase(
            List<ProjectRecommendation> savedRecommendations) {

        RecommendationResponse response =
                new RecommendationResponse();

        List<RecommendationResponse.ProjectRecommendation> projects =
                savedRecommendations.stream()
                        .map(saved -> {

                            try {

                                RecommendationResponse.ProjectRecommendation
                                        recommendation =
                                        objectMapper.readValue(
                                                saved.getRecommendationData(),
                                                RecommendationResponse
                                                        .ProjectRecommendation.class
                                        );

                                recommendation.setId(
                                        saved.getId()
                                );

                                return recommendation;

                            } catch (JsonProcessingException e) {

                                throw new RuntimeException(
                                        "Failed to read saved project recommendation.",
                                        e
                                );
                            }

                        })
                        .toList();

        response.setProjects(projects);

        return response;
    }

    // -------------------------------------------------------------
    // SELECT ACTIVE PROJECT
    // -------------------------------------------------------------

    @Transactional
    public void selectProject(
            String email,
            Long recommendationId) {

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException("User not found.")
                        );

        ProjectRecommendation recommendation =
                projectRecommendationRepository
                        .findById(recommendationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recommendation not found."
                                )
                        );

        // ---------------------------------------------------------
        // Security check:
        // A user can only select their own recommendation.
        // ---------------------------------------------------------

        if (!recommendation.getUser().getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "You cannot select another user's recommendation."
            );
        }

        // ---------------------------------------------------------
        // Find existing active project
        // ---------------------------------------------------------

        ActiveProject activeProject =
                activeProjectRepository
                        .findByUser(user)
                        .orElse(null);

        if (activeProject == null) {

            activeProject =
                    ActiveProject.builder()
                            .user(user)
                            .recommendation(recommendation)
                            .selectedAt(LocalDateTime.now())
                            .build();

        } else {

            activeProject.setRecommendation(
                    recommendation
            );

            activeProject.setSelectedAt(
                    LocalDateTime.now()
            );
        }

        activeProjectRepository.save(activeProject);
    }

    // -------------------------------------------------------------
    // GET ACTIVE PROJECT
    // -------------------------------------------------------------

    @Transactional(readOnly = true)
    public RecommendationResponse.ProjectRecommendation getActiveProject(
            String email) {

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException("User not found.")
                        );

        ActiveProject activeProject =
                activeProjectRepository
                        .findByUser(user)
                        .orElse(null);

        if (activeProject == null) {
            return null;
        }

        try {

            RecommendationResponse.ProjectRecommendation recommendation =
                    objectMapper.readValue(
                            activeProject
                                    .getRecommendation()
                                    .getRecommendationData(),
                            RecommendationResponse
                                    .ProjectRecommendation.class
                    );

            recommendation.setId(
                    activeProject
                            .getRecommendation()
                            .getId()
            );

            return recommendation;

        } catch (JsonProcessingException e) {

            throw new RuntimeException(
                    "Failed to read active project.",
                    e
            );
        }
    }
}