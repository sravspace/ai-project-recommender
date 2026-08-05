package com.project.aiprojectrecommender.profile.dto;

import com.project.aiprojectrecommender.enums.ExperienceLevel;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {

    private String fullName;

    private String bio;

    private ExperienceLevel experienceLevel;

    private String goals;

    private String interests;

    private String timeAvailability;

    private String githubUrl;

    private String linkedinUrl;

    private Boolean profileCompleted;

}