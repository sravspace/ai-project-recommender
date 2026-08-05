package com.project.aiprojectrecommender.repository;

import com.project.aiprojectrecommender.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository
        extends JpaRepository<UserProfile, Long> {
}