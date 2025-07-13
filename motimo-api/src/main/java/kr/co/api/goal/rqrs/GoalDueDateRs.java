package kr.co.api.goal.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record GoalDueDateRs(
        @Schema(description = "목표 완료 날짜 개월수로 설정 여부")
        boolean isMonth,

        @Schema(description = "목표 완료 개월수")
        Integer month,

        @Schema(description = "목표 완료 날짜", type = "date")
        LocalDate dueDate
) {

    public static GoalDueDateRs of(Integer month, LocalDate dueDate) {
        if (month != null) {
            return new GoalDueDateRs(true, month, dueDate);
        }
        return new GoalDueDateRs(false, null, dueDate);
    }
}
