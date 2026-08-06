package com.project.aiprojectrecommender.skills.controller;

import com.project.aiprojectrecommender.skills.dto.UserSkillRequest;
import com.project.aiprojectrecommender.skills.dto.UserSkillResponse;
import com.project.aiprojectrecommender.skills.service.UserSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile/skills")
@RequiredArgsConstructor
public class UserSkillController {

    private final UserSkillService userSkillService;

    @GetMapping
    public List<UserSkillResponse> getMySkills(
            Authentication authentication) {

        return userSkillService.getMySkills(authentication);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addSkill(
            Authentication authentication,
            @RequestBody UserSkillRequest request) {

        userSkillService.addSkill(authentication, request);

    }

}