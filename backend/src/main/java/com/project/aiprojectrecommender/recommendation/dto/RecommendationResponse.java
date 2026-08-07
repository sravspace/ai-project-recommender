package com.project.aiprojectrecommender.recommendation.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecommendationResponse {

    private List<ProjectRecommendation> projects;

    @Getter
    @Setter
    public static class ProjectRecommendation {

        private String title;
        private String description;
        private String difficulty;
        private String estimatedTime;
        private String whyThisProject;
        private String resumeValue;
        private String learningOutcome;

        private List<String> technologies;
        private List<String> skillsToLearn;
        private List<String> prerequisites;
        private List<String> stretchGoals;
    }
}