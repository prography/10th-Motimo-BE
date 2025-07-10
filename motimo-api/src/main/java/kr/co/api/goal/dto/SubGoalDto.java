package kr.co.api.goal.dto;

import java.util.List;
import java.util.UUID;
import kr.co.domain.todo.dto.TodoItem;

public record SubGoalDto(
        UUID id,
        String title,
        List<TodoItem> todos
) {

}
