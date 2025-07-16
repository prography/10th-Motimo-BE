package kr.co.api.group.rqrs.message;

import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
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
        },
        discriminatorProperty = "type",
        discriminatorMapping = {
                @DiscriminatorMapping(value = "JOIN", schema = GroupJoinContent.class),
                @DiscriminatorMapping(value = "LEAVE", schema = GroupLeaveContent.class),
                @DiscriminatorMapping(value = "TODO_COMPLETED", schema = TodoCompletedContent.class),
                @DiscriminatorMapping(value = "TODO_RESULT_SUBMITTED", schema = TodoResultSubmittedContent.class)
        },
        description = """
                        그룹 메시지 내용 
                        - JOIN: 그룹 참여 메시지UUID 
                        - LEAVE: 그룹 탈퇴 메시지
                        - TODO_COMPLETED: 할일 완료 메시지 (todoId, todoTitle 포함)
                        - TODO_RESULT_SUBMITTED: 할일 결과 제출 메시지 (todoId, todoTitle, result(emotion, content, fileUrl) 포함)
                """
)
public record GroupMessageContentRs(
        @Schema(description = "메시지 내용", requiredMode = Schema.RequiredMode.REQUIRED)
        GroupMessageContent content
) {

}
