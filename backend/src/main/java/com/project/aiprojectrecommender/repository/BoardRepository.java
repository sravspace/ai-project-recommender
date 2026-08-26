package com.project.aiprojectrecommender.repository;

import com.project.aiprojectrecommender.entity.Board;
import com.project.aiprojectrecommender.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository
        extends JpaRepository<Board, Long> {

    Optional<Board> findByProject(
            Project project
    );

    boolean existsByProject(
            Project project
    );
}