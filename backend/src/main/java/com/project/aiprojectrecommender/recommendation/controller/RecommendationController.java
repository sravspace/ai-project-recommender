package com.project.aiprojectrecommender.recommendation.controller;

import com.project.aiprojectrecommender.recommendation.dto.RecommendationResponse;
import com.project.aiprojectrecommender.recommendation.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/me")
    public RecommendationResponse recommend(
            Authentication authentication) {

        String email = authentication.getName();

        return recommendationService.recommendProjects(email);
    }

    @PostMapping("/select/{recommendationId}")
    @ResponseStatus(HttpStatus.OK)
    public void selectProject(
            @PathVariable Long recommendationId,
            Authentication authentication) {

        String email = authentication.getName();

        recommendationService.selectProject(
                email,
                recommendationId
        );
    }

    @GetMapping("/active")
    public RecommendationResponse.ProjectRecommendation getActiveProject(
            Authentication authentication) {

        String email = authentication.getName();

        return recommendationService.getActiveProject(email);
    }
}