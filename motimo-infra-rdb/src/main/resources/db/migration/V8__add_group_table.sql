CREATE TABLE group_users
(
    group_id    UUID NOT NULL,
    user_id     UUID NOT NULL,
    joined_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    isExited    BOOLEAN DEFAULT FALSE
);

CREATE TABLE groups
(
    id           UUID PRIMARY KEY,
    finishedDate TIMESTAMP,
    is_deleted   BOOLEAN NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
);

ALTER TABLE group_users
    ADD CONSTRAINT fk_group_users_on_group FOREIGN KEY (group_id) REFERENCES groups (id);