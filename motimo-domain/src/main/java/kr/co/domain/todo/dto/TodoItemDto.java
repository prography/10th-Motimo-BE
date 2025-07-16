package kr.co.domain.todo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.domain.todo.TodoStatus;

public record TodoItemDto(
        UUID id,
        String title,
        LocalDate date,
        TodoStatus status,
        LocalDateTime createdAt,
        TodoResultItemDto todoResultItem
) {

    public TodoItemDto withTodoResultItem(TodoResultItemDto todoResultItem) {
        return new TodoItemDto(id, title, date, status, createdAt, todoResultItem);
    }
}
