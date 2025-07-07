-- group_message 테이블 생성
CREATE TABLE group_message
(
    id             UUID PRIMARY KEY,
    group_id       UUID        NOT NULL,
    user_id        UUID        NOT NULL,
    type           VARCHAR(50) NOT NULL,
    reference_type VARCHAR(50),
    reference_id   UUID,
    send_at        TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted     BOOLEAN     NOT NULL DEFAULT FALSE
);

-- reaction 테이블 생성
CREATE TABLE reaction
(
    id            UUID PRIMARY KEY,
    user_id       UUID        NOT NULL,
    message_id    UUID        NOT NULL,
    reaction_type VARCHAR(50) NOT NULL,
    created_at    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reaction_message FOREIGN KEY (message_id) REFERENCES group_message (id)
);