package kr.co.api.group.rqrs.message;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import kr.co.api.group.service.dto.NewMessageDto;

@Schema(description = "새 메시지 카운트 및 최신 메세지 커서 응답")
public record NewMessageRs(
        @Schema(description = "새 메시지 존재 여부")
        boolean hasNewMessages,
        @Schema(description = "새 메시지 개수")
        int newMessageCount,
        @Schema(description = "가장 최신 메시지 시각", type = "date-time")
        LocalDateTime latestMessageTime,
        @Schema(description = "가장 최신 메시지 커서", example = "MDE5...")
        String latestMessageCursor
) {

    public static NewMessageRs from(NewMessageDto dto) {
        return new NewMessageRs(dto.hasNewMessages(), dto.newMessageCount(),
                dto.latestMessageTime(), dto.latestMessageCursor());
    }

}