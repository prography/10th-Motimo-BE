package kr.co.api.todo.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import kr.co.domain.todo.Emotion;
import kr.co.domain.todo.dto.TodoResultItemDto;

public record TodoResultRs(
        @Schema(description = "투두 결과 Id", example = "0197157f-aea4-77bb-8581-3213eb5bd2ae")
        UUID todoResultId,

        @Schema(description = "투두 진행 후 감정", example = "PROUD")
        Emotion emotion,

        @Schema(description = "투두 설명", example = "영단어 10개 이상 외우기를 했다.")
        String content,

        @Schema(description = "투두 기록 파일 url")
        String fileUrl,

        @Schema(description = "투두 기록 파일 이름")
        String fileName,

        @Schema(description = "투두 기록 파일 데이터 종류")
        String fileMimeType
) {

    public static TodoResultRs from(TodoResultItemDto result) {
        if (result == null) {
            return null;
        }
        return new TodoResultRs(result.id(), result.emotion(), result.content(), result.fileUrl(),
                result.fileName(), result.fileMimeType());
    }
}
