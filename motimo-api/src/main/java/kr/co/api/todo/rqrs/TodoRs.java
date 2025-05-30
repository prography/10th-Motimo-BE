package kr.co.api.todo.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.domain.todo.Todo;

import java.time.LocalDate;
import java.util.UUID;

public record TodoRs(
        @Schema(description = "투두 Id", example = "0197157f-aea4-77bb-8581-3213eb5bd2ae")
        UUID id,

        @Schema(description = "투두 제목", example = "영단어 10개 이상 외우기")
        String title,

        @Schema(description = "투두 완료 날짜", type = "date")
        LocalDate date
) {
        public static TodoRs of(Todo todo) {
                return new TodoRs(todo.getId(), todo.getTitle(), todo.getDate());
        }
}
