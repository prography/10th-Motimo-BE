-- 1. todo 테이블의 author_id를 user_id로 변경
ALTER TABLE todo RENAME COLUMN author_id TO user_id;

-- 2. todo_result 테이블에 user_id 컬럼 추가
ALTER TABLE todo_result ADD COLUMN user_id UUID NOT NULL;

-- 3. 기존 데이터에 대해 user_id 값 설정 (todo 테이블의 user_id를 참조)
UPDATE todo_result
SET user_id = (
    SELECT t.user_id
    FROM todo t
    WHERE t.id = todo_result.todo_id
);

-- 4. todo_result 테이블의 todo_id에 unique 제약조건 추가
ALTER TABLE todo_result ADD CONSTRAINT uk_todo_result_todo_id UNIQUE (todo_id);