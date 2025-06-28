package kr.co.api.goal.rqrs;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record GoalUpdateRq(
        @NotNull
        @Size(min = 1, max = 20)
        @Schema(description = "목표 이름", example = "자격증 따기")
        String title,

        @NotNull
        @Schema(description = "개월 수로 기간 설정 여부")
        boolean isPeriodByMonth,

        @Size(min = 1, max = 12)
        @Schema(description = "목표 개월 수")
        Integer month,

        @Schema(description = "목표 완료 날짜", format = "date")
        LocalDate dueDate,

        @Schema(description = "수정/생성할 세부 목표 목록")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        List<SubGoalUpdateRq> subGoals,

        @Schema(description = "삭제할 세부 목표 아이디 목록")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        Set<UUID> deletedSubGoalIds
) {

}
