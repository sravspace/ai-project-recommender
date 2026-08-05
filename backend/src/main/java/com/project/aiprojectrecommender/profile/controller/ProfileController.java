package com.project.aiprojectrecommender.profile.controller;

import com.project.aiprojectrecommender.profile.dto.ProfileResponse;
import com.project.aiprojectrecommender.profile.dto.ProfileUpdateRequest;
import com.project.aiprojectrecommender.profile.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/me")
    public ProfileResponse getMyProfile(Authentication authentication) {

        return profileService.getMyProfile(authentication);

    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateProfile(
            Authentication authentication,
            @Valid @RequestBody ProfileUpdateRequest request) {

        profileService.updateProfile(authentication, request);

    }

}