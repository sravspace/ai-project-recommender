export interface EstimatedTime {
    learningHours: number;
    buildHours: number;
    totalWeeks: number;
}

export interface ProjectRecommendation {
    title: string;
    description: string;
    difficulty: string;
    isStretch: boolean;

    estimatedTime: EstimatedTime;
    feasibilitySummary: string;

    whyItFits: string[];
    resumeSkills: string[];
    youWillLearn: string[];

    technologies: string[];
    existingSkills: string[];
    skillGaps: string[];
    prerequisites: string[];
    stretchGoals: string[];
}

export interface RecommendationResponse {
    projects: ProjectRecommendation[];
}
