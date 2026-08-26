package com.project.aiprojectrecommender.roadmap.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.aiprojectrecommender.entity.ActiveProject;
import com.project.aiprojectrecommender.entity.Board;
import com.project.aiprojectrecommender.entity.Milestone;
import com.project.aiprojectrecommender.entity.Project;
import com.project.aiprojectrecommender.entity.Task;
import com.project.aiprojectrecommender.entity.User;
import com.project.aiprojectrecommender.repository.ActiveProjectRepository;
import com.project.aiprojectrecommender.repository.BoardRepository;
import com.project.aiprojectrecommender.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoadmapTemplateService {

    private final ActiveProjectRepository activeProjectRepository;
    private final ProjectRepository projectRepository;
    private final BoardRepository boardRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public Project generateRoadmap(User user) {

        ActiveProject activeProject =
                activeProjectRepository.findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No active project found."
                                )
                        );

        Project existingProject =
                projectRepository.findByActiveProject(activeProject)
                        .orElse(null);

        if (existingProject != null) {
            return existingProject;
        }

        String recommendationData =
                activeProject
                        .getRecommendation()
                        .getRecommendationData();

        Project project =
                Project.builder()
                        .activeProject(activeProject)
                        .title(
                                activeProject
                                        .getRecommendation()
                                        .getTitle()
                        )
                        .description(
                                extractField(
                                        recommendationData,
                                        "description"
                                )
                        )
                        .difficulty(
                                extractField(
                                        recommendationData,
                                        "difficulty"
                                )
                        )
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

        project = projectRepository.save(project);

        List<Milestone> milestones =
                createMilestones(project);

        project.setMilestones(milestones);

        Board board =
                Board.builder()
                        .project(project)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

        boardRepository.save(board);

        project.setBoard(board);

        return project;
    }

    private List<Milestone> createMilestones(
            Project project) {

        List<Milestone> milestones =
                new ArrayList<>();

        Milestone setup =
                createMilestone(
                        project,
                        "Setup & Planning",
                        "Set up the project foundation and define the implementation plan.",
                        1
                );

        setup.setTasks(
                createTasks(
                        setup,
                        List.of(
                                "Define project scope",
                                "Set up the development environment",
                                "Create the initial project structure",
                                "Configure required dependencies"
                        )
                )
        );

        milestones.add(setup);

        Milestone core =
                createMilestone(
                        project,
                        "Core Development",
                        "Build the main functionality of the selected project.",
                        2
                );

        core.setTasks(
                createTasks(
                        core,
                        List.of(
                                "Design the core data model",
                                "Implement the main application logic",
                                "Build the required API or application features",
                                "Connect the main components"
                        )
                )
        );

        milestones.add(core);

        Milestone testing =
                createMilestone(
                        project,
                        "Testing & Refinement",
                        "Test the project and improve reliability and usability.",
                        3
                );

        testing.setTasks(
                createTasks(
                        testing,
                        List.of(
                                "Write tests for core functionality",
                                "Test edge cases and error handling",
                                "Fix discovered issues",
                                "Refine the user experience"
                        )
                )
        );

        milestones.add(testing);

        Milestone deployment =
                createMilestone(
                        project,
                        "Polish & Deployment",
                        "Prepare the completed project for demonstration and deployment.",
                        4
                );

        deployment.setTasks(
                createTasks(
                        deployment,
                        List.of(
                                "Add final documentation",
                                "Clean up the codebase",
                                "Prepare deployment configuration",
                                "Deploy the project"
                        )
                )
        );

        milestones.add(deployment);

        return milestones;
    }

    private Milestone createMilestone(
            Project project,
            String title,
            String description,
            int order) {

        return Milestone.builder()
                .project(project)
                .title(title)
                .description(description)
                .milestoneOrder(order)
                .status("TODO")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private List<Task> createTasks(
            Milestone milestone,
            List<String> taskTitles) {

        List<Task> tasks =
                new ArrayList<>();

        int order = 1;

        for (String title : taskTitles) {

            Task task =
                    Task.builder()
                            .milestone(milestone)
                            .title(title)
                            .description(null)
                            .taskOrder(order)
                            .status("TODO")
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();

            tasks.add(task);

            order++;
        }

        return tasks;
    }

    private String extractField(
            String recommendationData,
            String fieldName) {

        try {

            JsonNode json =
                    objectMapper.readTree(
                            recommendationData
                    );

            JsonNode field =
                    json.get(fieldName);

            if (field == null || field.isNull()) {
                return null;
            }

            return field.asText();

        } catch (Exception e) {

            return null;
        }
    }
    @Transactional(readOnly = true)
public Project getActiveRoadmap(User user) {

    ActiveProject activeProject =
            activeProjectRepository.findByUser(user)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "No active project found."
                            )
                    );

    return projectRepository
            .findByActiveProject(activeProject)
            .orElseThrow(() ->
                    new RuntimeException(
                            "No roadmap has been generated for the active project."
                    )
            );
}
}