package kr.co.api.subgoal.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record SubGoalIdRs(
        @Schema(description = "생성/수정된 세부 목표 아이디")
        UUID id
) {
}
