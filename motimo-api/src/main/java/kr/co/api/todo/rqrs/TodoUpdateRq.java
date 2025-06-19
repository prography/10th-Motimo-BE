package kr.co.api.todo.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record TodoUpdateRq(
        @Schema(description = "새로운 투두 타이틀", example = "영단어 외우기")
        String title,
        @Schema(description = "새로운 투두 완료 날짜", format = "date")
        LocalDate date) {

}
