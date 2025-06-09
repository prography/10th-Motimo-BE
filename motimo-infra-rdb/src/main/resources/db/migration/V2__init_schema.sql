-- 1. users 테이블
CREATE TABLE users
(
    id                UUID PRIMARY KEY,
    email             VARCHAR(255) UNIQUE,
    nickname          VARCHAR(255),
    profile_image_url TEXT,
    role              VARCHAR(50) NOT NULL,
    provider_type     VARCHAR(50) NOT NULL,
    provider_id       VARCHAR(255),
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted        BOOLEAN   DEFAULT FALSE
);

-- 2. refresh_token 테이블
CREATE TABLE refresh_token
(
    id         UUID PRIMARY KEY,
    token_id   UUID         NOT NULL UNIQUE,
    user_id    UUID         NOT NULL,
    token      VARCHAR(512) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
    -- 필요하다면 아래 FK 추가
    -- , FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 3. 투두 테이블
CREATE TABLE todo
(
    id          UUID PRIMARY KEY,
    sub_goal_id UUID NOT NULL,
    author_id   UUID NOT NULL,
    title       TEXT NOT NULL,
    date        DATE,
    completed   BOOLEAN   DEFAULT FALSE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted  BOOLEAN   DEFAULT FALSE
    -- 필요하다면 아래 FK 추가
    -- , FOREIGN KEY (author_id) REFERENCES users(id)
);

-- 4. todo_result 테이블
CREATE TABLE todo_result
(
    id         UUID PRIMARY KEY,
    todo_id    UUID NOT NULL,
    emotion    VARCHAR(50),
    content    TEXT,
    file_path  TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN   DEFAULT FALSE,
    FOREIGN KEY (todo_id) REFERENCES todo (id)
);