-- =========================================================
-- V4__create_projects_roadmap_schema.sql
-- Project + Roadmap + Board foundation
--
-- Tables:
--   1. projects
--   2. milestones
--   3. tasks
--   4. boards
-- =========================================================


-- =========================================================
-- PROJECTS
-- Represents the actual project selected by the user.
-- One project is created from one active project selection.
-- =========================================================

CREATE TABLE projects (

    id BIGSERIAL PRIMARY KEY,

    active_project_id BIGINT
        NOT NULL
        UNIQUE
        REFERENCES active_projects(id)
        ON DELETE CASCADE,

    title VARCHAR(255)
        NOT NULL,

    description TEXT,

    difficulty VARCHAR(20),

    created_at TIMESTAMP
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP
);



-- =========================================================
-- MILESTONES
-- Major phases of a project roadmap.
-- Example:
--   Setup
--   Core Feature
--   Polish
--   Deployment
-- =========================================================

CREATE TABLE milestones (

    id BIGSERIAL PRIMARY KEY,

    project_id BIGINT
        NOT NULL
        REFERENCES projects(id)
        ON DELETE CASCADE,

    title VARCHAR(255)
        NOT NULL,

    description TEXT,

    milestone_order INTEGER
        NOT NULL,

    status VARCHAR(20)
        NOT NULL
        DEFAULT 'TODO'
        CHECK (
            status IN (
                'TODO',
                'IN_PROGRESS',
                'DONE'
            )
        ),

    created_at TIMESTAMP
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_milestone_order
        UNIQUE(project_id, milestone_order)
);



-- =========================================================
-- TASKS
-- Individual pieces of work inside a milestone.
-- =========================================================

CREATE TABLE tasks (

    id BIGSERIAL PRIMARY KEY,

    milestone_id BIGINT
        NOT NULL
        REFERENCES milestones(id)
        ON DELETE CASCADE,

    title VARCHAR(255)
        NOT NULL,

    description TEXT,

    task_order INTEGER
        NOT NULL,

    status VARCHAR(20)
        NOT NULL
        DEFAULT 'TODO'
        CHECK (
            status IN (
                'TODO',
                'IN_PROGRESS',
                'DONE'
            )
        ),

    created_at TIMESTAMP
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_task_order
        UNIQUE(milestone_id, task_order)
);



-- =========================================================
-- BOARDS
-- One board per generated project.
-- Tasks are displayed on the board according to their status.
-- =========================================================

CREATE TABLE boards (

    id BIGSERIAL PRIMARY KEY,

    project_id BIGINT
        NOT NULL
        UNIQUE
        REFERENCES projects(id)
        ON DELETE CASCADE,

    created_at TIMESTAMP
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP
);



-- =========================================================
-- INDEXES
-- =========================================================

CREATE INDEX idx_projects_active_project
ON projects(active_project_id);

CREATE INDEX idx_milestones_project
ON milestones(project_id);

CREATE INDEX idx_tasks_milestone
ON tasks(milestone_id);

CREATE INDEX idx_tasks_status
ON tasks(status);

CREATE INDEX idx_boards_project
ON boards(project_id);