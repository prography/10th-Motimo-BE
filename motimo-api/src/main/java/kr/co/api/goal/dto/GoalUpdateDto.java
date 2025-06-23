package kr.co.api.goal.dto;

import kr.co.domain.goal.DueDate;

public record GoalUpdateDto(
        String title,
        DueDate dueDate
) {

}
