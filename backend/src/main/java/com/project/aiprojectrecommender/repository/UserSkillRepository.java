package com.project.aiprojectrecommender.repository;

import com.project.aiprojectrecommender.entity.User;
import com.project.aiprojectrecommender.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {

    List<UserSkill> findByUser(User user);

}