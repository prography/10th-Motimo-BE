package kr.co.api.group.service;

import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.domain.group.message.GroupMessageContent;
import kr.co.domain.group.message.MessageReference;

public record GroupMessageDto(
        UUID messageId,
        UUID userId,
        String userName,
        MessageReference messageReference,
        GroupMessageContent content,
        int reactionCount,
        boolean hasUserReacted,
        LocalDateTime sendAt
) {

}
