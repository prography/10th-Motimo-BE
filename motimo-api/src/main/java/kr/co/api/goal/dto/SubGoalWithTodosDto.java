package kr.co.api.goal.dto;

import java.util.List;
import java.util.UUID;
import kr.co.domain.todo.dto.TodoItemDto;

public record SubGoalWithTodosDto(
        UUID id,
        String title,
        boolean isCompleted,
        List<TodoItemDto> todos
) {

}