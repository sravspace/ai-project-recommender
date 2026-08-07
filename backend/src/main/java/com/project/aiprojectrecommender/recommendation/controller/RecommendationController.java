package com.project.aiprojectrecommender.recommendation.controller;

import com.project.aiprojectrecommender.recommendation.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/me")
    public String recommend(Authentication authentication) {

        String email = authentication.getName();

        return recommendationService.recommendProjects(email);

    }

} 
