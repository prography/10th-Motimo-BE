CREATE TABLE notifications
(
    id           UUID PRIMARY KEY,
    sender_id    UUID NOT NULL,
    receiver_id  UUID NOT NULL,
    title        VARCHAR(255),
    content      VARCHAR(255),
    type         VARCHAR(100),
    reference_id UUID,
    is_read      BOOLEAN   DEFAULT FALSE,
    is_deleted   BOOLEAN   DEFAULT FALSE,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);