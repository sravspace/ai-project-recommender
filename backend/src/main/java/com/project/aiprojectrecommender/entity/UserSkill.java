package com.project.aiprojectrecommender.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.aiprojectrecommender.enums.Proficiency;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "user_skills",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "skill_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    @JsonBackReference
    private Skill skill;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Proficiency proficiency;

    @Column(nullable = false)
    private Boolean verified;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false, updatable = true)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}