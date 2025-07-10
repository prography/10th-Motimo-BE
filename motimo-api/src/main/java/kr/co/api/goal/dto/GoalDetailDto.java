package kr.co.api.goal.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record GoalDetailDto(
        UUID id,
        String title,
        boolean isMonth,
        Integer month,
        LocalDate dueDate,
        float progress,
        LocalDateTime createdAt,
        boolean isCompleted,
        boolean isJoinedGroup
) {
}
