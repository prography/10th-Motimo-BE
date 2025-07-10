package kr.co.domain.todo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.domain.todo.TodoStatus;

public record TodoItem(
        UUID id,
        String title,
        LocalDate date,
        TodoStatus status,
        LocalDateTime createdAt,
        TodoResultItem todoResultItem
) {

    public TodoItem withTodoResultItem(TodoResultItem todoResultItem) {
        return new TodoItem(id, title, date, status, createdAt, todoResultItem);
    }
}
