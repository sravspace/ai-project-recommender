package com.project.aiprojectrecommender.repository;

import com.project.aiprojectrecommender.entity.Milestone;
import com.project.aiprojectrecommender.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository
        extends JpaRepository<Task, Long> {

    List<Task> findByMilestoneOrderByTaskOrder(
            Milestone milestone
    );
}