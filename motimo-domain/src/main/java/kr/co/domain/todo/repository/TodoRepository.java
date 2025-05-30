package kr.co.domain.todo.repository;

import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.todo.Todo;

import java.util.UUID;

public interface TodoRepository {

    Todo save(Todo toDo);

    Todo findById(UUID id);

    CustomSlice<Todo> findAllBySubGoalId(UUID subGoalId, int page, int size);

    CustomSlice<Todo> findAllByUserId(UUID userId, int page, int size);

    void deleteById(UUID id);
}
