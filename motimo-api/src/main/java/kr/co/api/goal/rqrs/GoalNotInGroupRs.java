package kr.co.api.goal.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record GoalNotInGroupRs(
        @Schema(description = "목표 아이디")
        UUID id,
        @Schema(description = "목표 제목")
        String title
) {

}
