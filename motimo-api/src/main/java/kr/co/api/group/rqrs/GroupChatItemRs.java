package kr.co.api.group.rqrs;

import java.util.UUID;
import kr.co.domain.group.ChatType;

public record GroupChatItemRs(
        ChatType chatType,
        UUID senderId,
        String senderName,
        GroupMessageRs message,
        int reactionCount
) {
}
