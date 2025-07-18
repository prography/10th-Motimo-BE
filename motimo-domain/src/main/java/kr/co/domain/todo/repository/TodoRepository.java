package kr.co.domain.todo.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.goal.dto.GoalTodoCount;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.dto.TodoItemDto;

public interface TodoRepository {

    Todo create(Todo todo);

    Todo findById(UUID id);

    CustomSlice<TodoItemDto> findIncompleteOrDateTodosBySubGoalId(UUID subGoalId, LocalDate date,
            int offset, int size);

    List<TodoItemDto> findAllByUserId(UUID userId);

    List<TodoItemDto> findAllBySubGoalId(UUID subGoalId);
    
    List<TodoItemDto> findAllIncompleteOrDateTodoBySubGoalId(UUID subGoalId, LocalDate today);

    CustomSlice<TodoItemDto> findAllBySubGoalIdWithSlice(UUID subGoalId, int offset, int size);

    List<GoalTodoCount> countTodosByGoalIds(List<UUID> goalId);

    boolean existsById(UUID id);

    Todo update(Todo todo);

    void deleteById(UUID id);

    void deleteAllTodoCascadeBySubGoalId(UUID subGoalId);

    List<Todo> findAllByIdsIn(Set<UUID> todoIds);
}
