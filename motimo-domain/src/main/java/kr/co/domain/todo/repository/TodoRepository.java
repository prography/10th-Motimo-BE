package kr.co.domain.todo.repository;

import java.util.List;
import java.util.UUID;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.dto.TodoSummary;

public interface TodoRepository {

    Todo save(Todo todo);

    Todo findById(UUID id);

    List<TodoSummary> findIncompleteOrTodayTodosBySubGoalId(UUID subGoalId);

    List<TodoSummary> findAllByUserId(UUID userId);

    boolean existsById(UUID id);

    void deleteById(UUID id);
}
