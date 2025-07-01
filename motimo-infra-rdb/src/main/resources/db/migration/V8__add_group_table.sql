CREATE TABLE group_user
(
    group_id    UUID NOT NULL,
    user_id     UUID NOT NULL,
    joined_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    isLeaved    BOOLEAN DEFAULT FALSE
);

CREATE TABLE groups
(
    id           UUID PRIMARY KEY,
    finishedDate TIMESTAMP,
    is_deleted   BOOLEAN NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
);

ALTER TABLE group_entity_users
    ADD CONSTRAINT fk_group_user_on_group FOREIGN KEY (group_id) REFERENCES groups (id);