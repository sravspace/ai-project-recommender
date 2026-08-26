package com.project.aiprojectrecommender.repository;

import com.project.aiprojectrecommender.entity.Milestone;
import com.project.aiprojectrecommender.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MilestoneRepository
        extends JpaRepository<Milestone, Long> {

    List<Milestone> findByProjectOrderByMilestoneOrder(
            Project project
    );
}