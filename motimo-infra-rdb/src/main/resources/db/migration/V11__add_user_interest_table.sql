CREATE TABLE IF NOT EXISTS user_interest (
    user_id       UUID        NOT NULL REFERENCES users(id),
    interest VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, interest)
);