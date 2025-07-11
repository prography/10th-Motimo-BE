package kr.co.api.group.rqrs.message;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.domain.group.message.content.GroupJoinContent;
import kr.co.domain.group.message.content.GroupLeaveContent;
import kr.co.domain.group.message.content.GroupMessageContent;
import kr.co.domain.group.message.content.TodoCompletedContent;
import kr.co.domain.group.message.content.TodoResultSubmittedContent;

@Schema(
        oneOf = {
                GroupJoinContent.class,
                GroupLeaveContent.class,
                TodoCompletedContent.class,
                TodoResultSubmittedContent.class,
        }
)
public record GroupMessageContentRs(
        GroupMessageContent content
) {

}
