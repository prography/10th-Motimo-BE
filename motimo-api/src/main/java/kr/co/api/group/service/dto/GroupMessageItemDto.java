package kr.co.api.group.service.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.domain.group.message.GroupMessage;
import kr.co.domain.group.message.content.GroupMessageContent;
import kr.co.domain.group.message.MessageReference;

public record GroupMessageItemDto(
        UUID messageId,
        UUID userId,
        String userName,
        MessageReference messageReference,
        GroupMessageContent content,
        int reactionCount,
        boolean hasUserReacted,
        LocalDateTime sendAt
) {

    public static GroupMessageItemDto of(GroupMessage groupMessage, String userName,
            GroupMessageContent content, boolean hasUserReacted) {
        return new GroupMessageItemDto(
                groupMessage.getId(),
                groupMessage.getUserId(),
                userName,
                groupMessage.getMessageReference(),
                content,
                groupMessage.getReactionCount(),
                hasUserReacted,
                groupMessage.getSendAt()
        );
    }

}
