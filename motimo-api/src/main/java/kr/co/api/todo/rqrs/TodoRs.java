package kr.co.api.todo.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.domain.todo.TodoStatus;
import kr.co.domain.todo.dto.TodoSummary;

public record TodoRs(
        @Schema(description = "투두 Id", example = "0197157f-aea4-77bb-8581-3213eb5bd2ae")
        UUID id,

        @Schema(description = "투두 제목", example = "영단어 10개 이상 외우기")
        String title,

        @Schema(description = "투두 완료 날짜", type = "date")
        LocalDate date,

        @Schema(description = "투두 상태", example = "COMPLETE")
        TodoStatus status,

        @Schema(description = "투두 기록 여부", example = "true")
        boolean hasResult,

        @Schema(description = "투두 생성 날짜", type = "date")
        LocalDateTime createdAt
) {

    public static TodoRs from(TodoSummary todoSummary) {
        return new TodoRs(todoSummary.id(), todoSummary.title(), todoSummary.date(),
                todoSummary.status(), todoSummary.hasResult(), todoSummary.createdAt());
    }
}
