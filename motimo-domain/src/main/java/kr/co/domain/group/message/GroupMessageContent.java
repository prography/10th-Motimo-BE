package kr.co.domain.group.message;

public sealed interface GroupMessageContent permits
        JoinContent, LeaveContent, TodoCompletedContent, TodoResultSubmittedContent {

    GroupMessageType getType();
}
