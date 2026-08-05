package com.project.aiprojectrecommender.profile.dto;

import com.project.aiprojectrecommender.enums.ExperienceLevel;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileUpdateRequest {

    @Size(max = 100)
    private String fullName;

    @Size(max = 500)
    private String bio;

    private ExperienceLevel experienceLevel;

    private String goals;

    private String interests;

    private String timeAvailability;

    private String githubUrl;

    private String linkedinUrl;

}