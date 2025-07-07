CREATE TABLE outbox_event
(
    id         UUID PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    payload    TEXT         NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
