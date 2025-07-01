package kr.co.api.group.rqrs.message;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import kr.co.api.group.rqrs.MessageContentRs;
import kr.co.domain.todo.Emotion;

public record TodoMessageContentRs(
        @Schema(description = "투두 아이디")
        UUID targetId,
        @Schema(description = "완료한 투두 제목")
        String todoTitle,
        @Schema(description = "투두 감정")
        Emotion emotion,
        @Schema(description = "투두 내용")
        String content,
        @Schema(description = "제출한 파일 링크")
        String fileUrl
) implements MessageContentRs {
}