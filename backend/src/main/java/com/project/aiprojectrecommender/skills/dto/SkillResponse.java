package com.project.aiprojectrecommender.skills.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillResponse {

    private Long id;

    private String name;

    private String category;

}