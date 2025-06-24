package kr.co.api.goal.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.UUID;
import kr.co.api.goal.dto.GoalItemDto;

public record GoalItemRs(
        @Schema(description = "목표 아이디")
        UUID id,

        @Schema(description = "목표 이름", example = "자격증 따기")
        String title,

        @Schema(description = "목표 완료 날짜", type = "date")
        LocalDate dueDate,

        @Schema(description = "목표 달성률 (%)", example = "24.5")
        float progress
) {

        public static GoalItemRs from(GoalItemDto dto) {
                return new GoalItemRs(
                        dto.id(),
                        dto.title(),
                        dto.dueDate(),
                        dto.progress()
                );
        }
}
