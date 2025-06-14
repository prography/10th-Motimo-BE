package kr.co.domain.todo.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.dto.TodoSummary;

public interface TodoRepository {

    Todo save(Todo todo);

    Todo findById(UUID id);

    List<TodoSummary> findIncompleteOrDateTodosBySubGoalId(UUID subGoalId, LocalDate date);

    List<TodoSummary> findAllByUserId(UUID userId);

    boolean existsById(UUID id);

    void deleteById(UUID id);
}
