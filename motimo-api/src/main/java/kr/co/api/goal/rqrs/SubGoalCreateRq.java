package kr.co.api.goal.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SubGoalCreateRq(
        @NotNull
        @Size(min = 1, max = 20)
        @Schema(description = "세부 목표 이름", example = "책 한 권 읽기")
        String title
) {

}
