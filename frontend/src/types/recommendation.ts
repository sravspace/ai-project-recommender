export interface ProjectRecommendation {
    title: string;
    description: string;
    difficulty: string;
    estimatedTime: string;
    whyThisProject: string;
    resumeValue: string;
    learningOutcome: string;
    technologies: string[];
    skillsToLearn: string[];
    prerequisites: string[];
    stretchGoals: string[];
}

export interface RecommendationResponse {
    projects: ProjectRecommendation[];
}