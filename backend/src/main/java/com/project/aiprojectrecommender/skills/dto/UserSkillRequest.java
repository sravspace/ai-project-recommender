package com.project.aiprojectrecommender.skills.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSkillRequest {

    private Long skillId;

    private String proficiency;

}