package kr.co.api.goal.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.UUID;
import kr.co.api.goal.dto.CompletedGoalItemDto;

public record CompletedGoalItemRs(
        @Schema(description = "목표 아이디")
        UUID id,

        @Schema(description = "목표 이름", example = "자격증 따기")
        String title,

        @Schema(description = "목표 기간", type = "date")
        LocalDate dueDate,

        @Schema(description = "전체 투두 개수", example = "15")
        long todoCount,

        @Schema(description = "완료된 투두 결과 개수", example = "15")
        long todoResultCount
) {

    public static CompletedGoalItemRs from(CompletedGoalItemDto dto) {
        return new CompletedGoalItemRs(
                dto.id(),
                dto.title(),
                dto.dueDate(),
                dto.todoCount(),
                dto.todoResultCount()
        );
    }
}