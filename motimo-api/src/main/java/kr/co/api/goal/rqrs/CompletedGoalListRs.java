package kr.co.api.goal.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import kr.co.api.goal.dto.CompletedGoalItemDto;

public record CompletedGoalListRs(
        @Schema(description = "완료된 목표 목록")
        List<CompletedGoalItemRs> goals
) {

    public static CompletedGoalListRs from(List<CompletedGoalItemDto> dtos) {
        return new CompletedGoalListRs(
                dtos.stream().map(CompletedGoalItemRs::from).toList()
        );
    }
}
