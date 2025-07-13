package kr.co.domain.todo.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import kr.co.domain.goal.dto.GoalTodoCount;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.dto.TodoItemDto;

public interface TodoRepository {

    Todo create(Todo todo);

    Todo findById(UUID id);

    List<TodoItemDto> findIncompleteOrDateTodosBySubGoalId(UUID subGoalId, LocalDate date);

    List<TodoItemDto> findAllByUserId(UUID userId);

    List<TodoItemDto> findAllBySubGoalId(UUID subGoalId);

    List<GoalTodoCount> countTodosByGoalIds(List<UUID> goalId);

    boolean existsById(UUID id);

    Todo update(Todo todo);

    void deleteById(UUID id);

    List<Todo> findAllByIdsIn(Set<UUID> todoIds);
}
