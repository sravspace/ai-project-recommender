package com.project.aiprojectrecommender.repository;

import com.project.aiprojectrecommender.entity.ActiveProject;
import com.project.aiprojectrecommender.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository
        extends JpaRepository<Project, Long> {

    Optional<Project> findByActiveProject(
            ActiveProject activeProject
    );

    boolean existsByActiveProject(
            ActiveProject activeProject
    );
}