package com.project.aiprojectrecommender.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "milestones",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_milestone_order",
                        columnNames = {"project_id", "milestone_order"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "milestone_order", nullable = false)
    private Integer milestoneOrder;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "TODO";

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(
            mappedBy = "milestone",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("taskOrder ASC")
    private List<Task> tasks;
}