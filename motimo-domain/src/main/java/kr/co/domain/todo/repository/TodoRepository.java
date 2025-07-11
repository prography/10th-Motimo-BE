package kr.co.domain.todo.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.dto.TodoSummary;

public interface TodoRepository {

    Todo create(Todo todo);

    Todo findById(UUID id);

    List<TodoSummary> findIncompleteOrDateTodosBySubGoalId(UUID subGoalId, LocalDate date);

    List<TodoSummary> findAllByUserId(UUID userId);

    boolean existsById(UUID id);

    Todo update(Todo todo);

    void deleteById(UUID id);

    List<Todo> findAllByIdsIn(Set<UUID> todoIds);
}
