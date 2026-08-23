package com.project.aiprojectrecommender.repository;

import com.project.aiprojectrecommender.entity.ProjectRecommendation;
import com.project.aiprojectrecommender.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRecommendationRepository
        extends JpaRepository<ProjectRecommendation, Long> {

    List<ProjectRecommendation> findByUserOrderByIdAsc(User user);

    void deleteByUser(User user);
}