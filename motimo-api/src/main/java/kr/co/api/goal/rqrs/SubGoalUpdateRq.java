package kr.co.api.goal.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record SubGoalUpdateRq(
        @Schema(description = "수정할 세부 목표 아이디 / 새로 생성이면 null", nullable = true)
        UUID id,

        @NotNull
        @Size(min = 1, max = 20)
        @Schema(description = "세부 목표 이름", example = "책 한 권 읽기")
        String title,

        @Schema(description = "세부 목표 순서")
        int order
) {

}
