package kr.co.domain.todo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.domain.todo.TodoStatus;

public record TodoSummary(
        UUID id,
        String title,
        LocalDate date,
        TodoStatus status,
        LocalDateTime createdAt,
        boolean hasResult
) {

}
