package com.project.aiprojectrecommender.profile.service;

import com.project.aiprojectrecommender.entity.User;
import com.project.aiprojectrecommender.entity.UserProfile;
import com.project.aiprojectrecommender.profile.dto.ProfileResponse;
import com.project.aiprojectrecommender.profile.dto.ProfileUpdateRequest;
import com.project.aiprojectrecommender.repository.UserProfileRepository;
import com.project.aiprojectrecommender.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    public ProfileResponse getMyProfile(Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found."));

        UserProfile profile = userProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found."));

        return ProfileResponse.builder()
                .fullName(profile.getFullName())
                .bio(profile.getBio())
                .experienceLevel(profile.getExperienceLevel())
                .goals(profile.getGoals())
                .interests(profile.getInterests())
                .timeAvailability(profile.getTimeAvailability())
                .githubUrl(profile.getGithubUrl())
                .linkedinUrl(profile.getLinkedinUrl())
                .profileCompleted(profile.getProfileCompleted())
                .build();
    }

    public void updateProfile(
            Authentication authentication,
            ProfileUpdateRequest request) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found."));

        UserProfile profile = userProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found."));

        profile.setFullName(request.getFullName());
        profile.setBio(request.getBio());
        profile.setExperienceLevel(request.getExperienceLevel());
        profile.setGoals(request.getGoals());
        profile.setInterests(request.getInterests());
        profile.setTimeAvailability(request.getTimeAvailability());
        profile.setGithubUrl(request.getGithubUrl());
        profile.setLinkedinUrl(request.getLinkedinUrl());

        profile.setProfileCompleted(true);

        userProfileRepository.save(profile);
    }
}