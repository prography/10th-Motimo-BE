package kr.co.api.group.rqrs.message;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.domain.group.message.GroupJoinContent;
import kr.co.domain.group.message.GroupLeaveContent;
import kr.co.domain.group.message.GroupMessageContent;
import kr.co.domain.group.message.TodoCompletedContent;
import kr.co.domain.group.message.TodoResultSubmittedContent;

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
