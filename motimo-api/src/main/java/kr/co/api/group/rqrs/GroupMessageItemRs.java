package kr.co.api.group.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import kr.co.domain.group.MessageType;

public record GroupMessageItemRs(
        @Schema(description = "메세지 타입 (TODO, ENTER)")
        MessageType messageType,
        @Schema(description = "보낸 사용자 아이디")
        UUID senderId,
        @Schema(description = "보낸 사용자 닉네임")
        String senderName,
        @Schema(description = "메세지 내용")
        MessageContentRs message,
        @Schema(description = "리액션 갯수")
        int reactionCount
) {
}
