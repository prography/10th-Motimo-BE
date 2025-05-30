package kr.co.api.goal.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SubGoalUpdateRq(
        @Schema(description = "수정할 세부 목표 아이디")
        String id,

        @NotNull
        @Size(min = 1, max = 20)
        @Schema(description = "세부 목표 이름", example = "책 한 권 읽기")
        String title,

        @Size(min = 1, max = 3)
        @Schema(description = "세부 목표 중요도 (상: 1, 하:3)")
        int importance
) {

}
