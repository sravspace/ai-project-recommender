package com.project.aiprojectrecommender.skills.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillRequest {

    private String name;

    private String category;

}