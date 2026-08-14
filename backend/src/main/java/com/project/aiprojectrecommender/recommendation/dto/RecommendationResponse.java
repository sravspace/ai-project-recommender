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
        private boolean isStretch;

        private EstimatedTime estimatedTime;
        private String feasibilitySummary;

        private List<String> whyItFits;
        private List<String> resumeSkills;
        private List<String> youWillLearn;

        private List<String> technologies;
        private List<String> existingSkills;
        private List<String> skillGaps;
        private List<String> prerequisites;
        private List<String> stretchGoals;
    }

    @Getter
    @Setter
    public static class EstimatedTime {

        private Integer learningHours;
        private Integer buildHours;
        private Integer totalWeeks;
    }
}
