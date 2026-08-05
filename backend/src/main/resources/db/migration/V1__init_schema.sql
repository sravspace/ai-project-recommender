-- =========================================================
-- V1__init_schema.sql
-- AI Project Recommender
--
-- Week 1 / Day 3
--
-- Foundation Schema
-- Tables:
--   1. users
--   2. user_profiles
--   3. skills
--   4. user_skills
--
-- Future tables (generated_projects, boards, tasks,
-- quizzes, resume analysis, etc.) belong in later
-- Flyway migrations (V2__, V3__, ...).
--
-- IMPORTANT:
-- Never edit this file after it has been executed.
-- =========================================================


-- =========================================================
-- USERS
-- Authentication & Authorization
-- =========================================================

CREATE TABLE users (

    id BIGSERIAL PRIMARY KEY,

    email VARCHAR(255)
        NOT NULL
        UNIQUE,

    password_hash VARCHAR(255)
        NOT NULL,

    role VARCHAR(20)
        NOT NULL
        DEFAULT 'USER'
        CHECK (role IN ('USER', 'ADMIN')),

    last_login TIMESTAMP,

    created_at TIMESTAMP
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP
);



-- =========================================================
-- USER PROFILES
-- Stores information used by the recommendation engine.
-- One profile per user.
-- =========================================================

CREATE TABLE user_profiles (

    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT
        NOT NULL
        UNIQUE
        REFERENCES users(id)
        ON DELETE CASCADE,

    full_name VARCHAR(100),

    college VARCHAR(150),

    department VARCHAR(100),

    year INTEGER,

    bio TEXT,

    goals TEXT,

    interests TEXT,

    experience_level VARCHAR(20)
        CHECK (
            experience_level IN
            ('BEGINNER', 'INTERMEDIATE', 'ADVANCED')
        ),

    time_availability VARCHAR(50),

    github_url TEXT,

    linkedin_url TEXT,

    resume_url TEXT,

    profile_completed BOOLEAN
        NOT NULL
        DEFAULT FALSE,

    created_at TIMESTAMP
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP
);



-- =========================================================
-- SKILLS
-- Master list of all supported skills.
-- Never duplicate skill names.
-- =========================================================

CREATE TABLE skills (

    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(100)
        NOT NULL
        UNIQUE,

    category VARCHAR(50)
);



-- =========================================================
-- USER SKILLS
-- Many-to-many bridge between users and skills.
-- Week 5 quiz will update "verified".
-- =========================================================

CREATE TABLE user_skills (

    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT
        NOT NULL
        REFERENCES users(id)
        ON DELETE CASCADE,

    skill_id BIGINT
        NOT NULL
        REFERENCES skills(id)
        ON DELETE CASCADE,

    proficiency VARCHAR(20)
        NOT NULL
        DEFAULT 'BEGINNER'
        CHECK (
            proficiency IN
            (
                'BEGINNER',
                'INTERMEDIATE',
                'ADVANCED',
                'EXPERT'
            )
        ),

    verified BOOLEAN
        NOT NULL
        DEFAULT FALSE,

    created_at TIMESTAMP
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_user_skill
        UNIQUE(user_id, skill_id)
);



-- =========================================================
-- INDEXES
-- =========================================================

CREATE INDEX idx_user_profiles_user
ON user_profiles(user_id);

CREATE INDEX idx_user_skills_user
ON user_skills(user_id);

CREATE INDEX idx_user_skills_skill
ON user_skills(skill_id);