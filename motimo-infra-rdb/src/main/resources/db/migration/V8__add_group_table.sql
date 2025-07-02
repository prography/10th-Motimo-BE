CREATE TABLE groups
(
    id            UUID PRIMARY KEY,
    is_deleted    BOOLEAN NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    finished_date TIMESTAMP
);

CREATE TABLE group_users
(
    id                     UUID PRIMARY KEY,
    user_id                UUID NOT NULL,
    goal_id                UUID NOT NULL,
    group_id               UUID NOT NULL,
    joined_date            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_notification_active BOOLEAN DEFAULT TRUE,
    created_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted             BOOLEAN DEFAULT FALSE,
);

ALTER TABLE group_users
    ADD CONSTRAINT FK_GROUP_USERS_ON_GROUP FOREIGN KEY (group_id) REFERENCES groups (id);