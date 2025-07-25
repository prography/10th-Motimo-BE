package kr.co.api.goal.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import kr.co.api.goal.dto.GoalDetailDto;

public record GoalDetailRs(
        @Schema(description = "목표 아이디")
        UUID id,

        @Schema(description = "목표 이름", example = "자격증 따기")
        String title,

        GoalDueDateRs dueDate,

        @Schema(description = "목표 달성률 (%)", example = "24.5")
        float progress,

        @Schema(description = "목표 완료 여부")
        boolean isCompleted,

        @Schema(description = "그룹 참여 여부")
        boolean isJoinedGroup,

        @Schema(description = "그룹 아이디")
        UUID groupId
) {

    public static GoalDetailRs from(GoalDetailDto dto) {
        return new GoalDetailRs(
                dto.id(),
                dto.title(),
                GoalDueDateRs.of(dto.month(), dto.dueDate()),
                dto.progress(),
                dto.isCompleted(),
                dto.isJoinedGroup(),
                dto.groupId()
        );
    }
}
