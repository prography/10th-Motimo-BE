package kr.co.api.todo.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record TodoCreateRq(
        @NotNull
        @Schema(description = "세부 목표 ID", example = "0197157f-aea4-77bb-8581-3213eb5bd2ae")
        UUID subGoalId,

        @NotNull
        @Size(min = 1, max = 20)
        @Schema(description = "투두 제목", example = "영단어 10개 이상 외우기")
        String title,

        @Schema(description = "투두 완료 날짜", format = "date")
        LocalDate date
) {
}
