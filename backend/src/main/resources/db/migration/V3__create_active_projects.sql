CREATE TABLE active_projects (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL UNIQUE,

    recommendation_id BIGINT NOT NULL,

    selected_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_active_projects_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_active_projects_recommendation
        FOREIGN KEY (recommendation_id)
        REFERENCES project_recommendations(id)
        ON DELETE CASCADE
);