package kr.co.api.todo.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kr.co.domain.todo.Emotion;

public record TodoResultRq(
        @NotNull
        @Schema(description = "투두 진행 후 감정", example = "HAPPY")
        Emotion emotion,

        @Schema(description = "투두 내용", example = "영어 단어를 10개를 외웠다.")
        String content
) {

}
