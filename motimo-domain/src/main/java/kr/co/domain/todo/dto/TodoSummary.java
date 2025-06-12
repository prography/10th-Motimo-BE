package kr.co.domain.todo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TodoSummary(
        UUID id,
        String title,
        LocalDate date,
        boolean completed,
        LocalDateTime createdAt,
        boolean hasResult
) {

}
