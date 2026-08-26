package com.project.aiprojectrecommender.roadmap.controller;

import com.project.aiprojectrecommender.entity.Milestone;
import com.project.aiprojectrecommender.entity.Project;
import com.project.aiprojectrecommender.entity.Task;
import com.project.aiprojectrecommender.entity.User;
import com.project.aiprojectrecommender.repository.UserRepository;
import com.project.aiprojectrecommender.roadmap.dto.RoadmapResponse;
import com.project.aiprojectrecommender.roadmap.service.RoadmapTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roadmap")
@RequiredArgsConstructor
public class RoadmapController {

    private final RoadmapTemplateService roadmapTemplateService;
    private final UserRepository userRepository;

    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.CREATED)
    public RoadmapResponse generateRoadmap(
            Authentication authentication) {

        User user = getAuthenticatedUser(authentication);

        Project project =
                roadmapTemplateService.generateRoadmap(user);

        return buildResponse(project);
    }

    @GetMapping("/active")
    public RoadmapResponse getActiveRoadmap(
            Authentication authentication) {

        User user = getAuthenticatedUser(authentication);

        Project project =
                roadmapTemplateService.getActiveRoadmap(user);

        return buildResponse(project);
    }

    private User getAuthenticatedUser(
            Authentication authentication) {

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found."
                        )
                );
    }

    private RoadmapResponse buildResponse(
            Project project) {

        List<RoadmapResponse.MilestoneResponse> milestones =
                project.getMilestones()
                        .stream()
                        .map(this::buildMilestoneResponse)
                        .toList();

        Long boardId =
                project.getBoard() != null
                        ? project.getBoard().getId()
                        : null;

        return RoadmapResponse.builder()
                .id(project.getId())
                .activeProjectId(
                        project.getActiveProject().getId()
                )
                .title(project.getTitle())
                .description(project.getDescription())
                .difficulty(project.getDifficulty())
                .milestones(milestones)
                .boardId(boardId)
                .build();
    }

    private RoadmapResponse.MilestoneResponse
    buildMilestoneResponse(
            Milestone milestone) {

        List<RoadmapResponse.TaskResponse> tasks =
                milestone.getTasks()
                        .stream()
                        .map(this::buildTaskResponse)
                        .toList();

        return RoadmapResponse.MilestoneResponse.builder()
                .id(milestone.getId())
                .title(milestone.getTitle())
                .description(milestone.getDescription())
                .milestoneOrder(
                        milestone.getMilestoneOrder()
                )
                .status(milestone.getStatus())
                .tasks(tasks)
                .build();
    }

    private RoadmapResponse.TaskResponse
    buildTaskResponse(Task task) {

        return RoadmapResponse.TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .taskOrder(task.getTaskOrder())
                .status(task.getStatus())
                .build();
    }
}