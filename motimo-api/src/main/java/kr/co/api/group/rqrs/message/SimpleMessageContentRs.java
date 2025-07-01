package kr.co.api.group.rqrs.message;

import java.util.UUID;
import kr.co.api.group.rqrs.MessageContentRs;

public record SimpleMessageContentRs(
        UUID targetId,
        String content
) implements MessageContentRs {
}