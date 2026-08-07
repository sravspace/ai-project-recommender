package com.project.aiprojectrecommender.recommendation.service;

import com.project.aiprojectrecommender.entity.User;
import com.project.aiprojectrecommender.entity.UserProfile;
import com.project.aiprojectrecommender.entity.UserSkill;
import com.project.aiprojectrecommender.llm.service.GeminiService;
import com.project.aiprojectrecommender.llm.util.PromptBuilder;
import com.project.aiprojectrecommender.repository.UserProfileRepository;
import com.project.aiprojectrecommender.repository.UserRepository;
import com.project.aiprojectrecommender.repository.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserSkillRepository userSkillRepository;

    private final PromptBuilder promptBuilder;
    private final GeminiService geminiService;

    public String recommendProjects(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));

        UserProfile profile = userProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found."));

        List<UserSkill> userSkills = userSkillRepository.findByUser(user);

        String prompt = promptBuilder.buildPrompt(profile, userSkills);

        return geminiService.generate(prompt);

    }

}