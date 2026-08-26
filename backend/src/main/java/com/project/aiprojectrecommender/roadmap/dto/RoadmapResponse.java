package com.project.aiprojectrecommender.roadmap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RoadmapResponse {

    private Long id;

    private Long activeProjectId;

    private String title;

    private String description;

    private String difficulty;

    private List<MilestoneResponse> milestones;

    private Long boardId;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MilestoneResponse {

        private Long id;

        private String title;

        private String description;

        private Integer milestoneOrder;

        private String status;

        private List<TaskResponse> tasks;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class TaskResponse {

        private Long id;

        private String title;

        private String description;

        private Integer taskOrder;

        private String status;
    }
}