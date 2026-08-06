package com.project.aiprojectrecommender.skills.controller;

import com.project.aiprojectrecommender.skills.dto.SkillRequest;
import com.project.aiprojectrecommender.skills.dto.SkillResponse;
import com.project.aiprojectrecommender.skills.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @GetMapping
    public List<SkillResponse> getAllSkills() {

        return skillService.getAllSkills();

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SkillResponse createSkill(
            @RequestBody SkillRequest request) {

        return skillService.createSkill(request);

    }

}