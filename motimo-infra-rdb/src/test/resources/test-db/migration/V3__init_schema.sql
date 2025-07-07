CREATE TABLE goals
(
    id           UUID  PRIMARY KEY,
    user_id      UUID,
    title        VARCHAR(255),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed    BOOLEAN NOT NULL,
    completed_at TIMESTAMP,
    "month"        INTEGER,
    due_date     date,
    is_deleted   BOOLEAN NOT NULL
);

CREATE TABLE sub_goals
(
    id                   UUID  PRIMARY KEY,
    goal_id              UUID,
    title                VARCHAR(255),
    importance           INTEGER NOT NULL,
    completed            BOOLEAN NOT NULL,
    completed_changed_at TIMESTAMP WITHOUT TIME ZONE,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted           BOOLEAN NOT NULL,
    FOREIGN KEY (goal_id) REFERENCES goals (id)
);
