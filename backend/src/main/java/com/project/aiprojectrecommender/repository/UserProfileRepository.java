package com.project.aiprojectrecommender.repository;

import com.project.aiprojectrecommender.entity.User;
import com.project.aiprojectrecommender.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUser(User user);

}