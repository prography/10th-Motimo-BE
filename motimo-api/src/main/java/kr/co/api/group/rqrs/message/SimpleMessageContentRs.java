package kr.co.api.group.rqrs.message;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import kr.co.api.group.rqrs.MessageContentRs;

public record SimpleMessageContentRs(
        @Schema(description = "링크된 아이디")
        UUID targetId,
        @Schema(description = "기본적인 내용")
        String content
) implements MessageContentRs {
}