package com.project.aiprojectrecommender.skills.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSkillResponse {

    private Long id;

    private Long skillId;

    private String skillName;

    private String category;

    private String proficiency;

    private Boolean verified;

}