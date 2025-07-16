-- 1. 기존 PK 삭제
ALTER TABLE reaction DROP CONSTRAINT reaction_pkey;

-- 2. id 컬럼 제거
ALTER TABLE reaction DROP COLUMN id;

-- 3. 복합 PK 설정
ALTER TABLE reaction
    ADD CONSTRAINT reaction_pkey PRIMARY KEY (user_id, message_id);