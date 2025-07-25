package kr.co.domain.group.message.content;

import kr.co.domain.group.message.GroupMessageType;

public sealed interface GroupMessageContent permits GoalTitleUpdatedContent, GroupJoinContent, GroupLeaveContent,
        MessageReactionContent, TodoCompletedContent, TodoResultSubmittedContent {
    GroupMessageType getType();
}
