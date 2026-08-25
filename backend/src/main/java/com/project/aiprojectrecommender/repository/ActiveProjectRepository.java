package com.project.aiprojectrecommender.repository;

import com.project.aiprojectrecommender.entity.ActiveProject;
import com.project.aiprojectrecommender.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActiveProjectRepository
        extends JpaRepository<ActiveProject, Long> {

    Optional<ActiveProject> findByUser(User user);

    boolean existsByUser(User user);
}