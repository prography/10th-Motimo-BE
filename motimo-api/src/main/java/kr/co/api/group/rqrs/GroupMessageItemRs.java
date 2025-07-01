package kr.co.api.group.rqrs;

import java.util.UUID;
import kr.co.domain.group.MessageType;

//
public record GroupMessageItemRs(
        MessageType messageType,
        UUID senderId,
        String senderName,
        MessageContentRs message,
        int reactionCount
) {
}
