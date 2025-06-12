package kr.co.domain.todo.repository;

import java.util.UUID;
import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.dto.TodoSummary;

public interface TodoRepository {

    Todo save(Todo todo);

    Todo findById(UUID id);

    CustomSlice<TodoSummary> findAllBySubGoalId(UUID subGoalId, int page, int size);

    CustomSlice<TodoSummary> findAllByUserId(UUID userId, int page, int size);

    boolean existsById(UUID id);

    void deleteById(UUID id);
}
