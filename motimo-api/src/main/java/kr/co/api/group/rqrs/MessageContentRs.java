package kr.co.api.group.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import kr.co.api.group.rqrs.message.SimpleMessageContentRs;
import kr.co.api.group.rqrs.message.TodoMessageContentRs;

@Schema(
        oneOf = {
                SimpleMessageContentRs.class,
                TodoMessageContentRs.class
        }
)
public interface MessageContentRs {
    UUID targetId();
    String content();
}

