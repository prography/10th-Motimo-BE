package kr.co.api.subgoal.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record TodoCreateRq(

        @NotNull
        @Size(min = 1, max = 20)
        @Schema(description = "투두 제목", example = "영단어 10개 이상 외우기")
        String title,

        @Schema(description = "투두 완료 날짜", format = "date")
        LocalDate date
) {

}
