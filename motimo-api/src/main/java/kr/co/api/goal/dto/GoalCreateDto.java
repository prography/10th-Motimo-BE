package kr.co.api.goal.dto;

import java.time.LocalDate;
import java.util.List;
import kr.co.api.goal.rqrs.GoalCreateRq;

public record GoalCreateDto(
        String title,

        boolean isPeriodByMonth,

        Integer month,

        LocalDate dueDate,

        List<SubGoalCreateDto> subGoals
) {

    public static GoalCreateDto from(GoalCreateRq rq) {
        return new GoalCreateDto(
                rq.title(),
                rq.isPeriodByMonth(),
                rq.month(),
                rq.dueDate(),
                rq.subGoals().stream().map(SubGoalCreateDto::from).toList()
        );
    }

}
