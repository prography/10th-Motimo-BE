package kr.co.api.group.service.dto;

import java.time.LocalDateTime;

public record NewMessageDto(
        boolean hasNewMessages,
        int newMessageCount,
        LocalDateTime latestMessageTime,
        String latestMessageCursor  // 최신 메시지의 커서
) {

}