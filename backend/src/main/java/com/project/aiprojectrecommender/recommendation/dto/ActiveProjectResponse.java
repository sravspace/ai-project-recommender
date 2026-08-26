package com.project.aiprojectrecommender.recommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ActiveProjectResponse {

    private Long id;

    private Long recommendationId;

    private String title;

    private LocalDateTime selectedAt;
}