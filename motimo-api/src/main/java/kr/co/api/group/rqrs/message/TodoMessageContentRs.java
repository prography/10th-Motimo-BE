package kr.co.api.group.rqrs.message;

import java.util.UUID;
import kr.co.api.group.rqrs.MessageContentRs;

public record TodoMessageContentRs(
        UUID targetId,
        String todoTitle,
        String content,
        String fileUrl
) implements MessageContentRs {
}