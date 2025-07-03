package kr.co.domain.group.message;

public sealed interface GroupMessageContent permits
        GroupJoinContent, GroupLeaveContent, TodoCompletedContent, TodoResultSubmittedContent {

    GroupMessageType getType();
}
