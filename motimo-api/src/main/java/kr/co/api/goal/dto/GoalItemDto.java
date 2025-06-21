package kr.co.api.goal.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record GoalItemDto(
        UUID id,
        String title,
        LocalDate dueDate,
        float progress,
        LocalDateTime createdAt
) {

}
