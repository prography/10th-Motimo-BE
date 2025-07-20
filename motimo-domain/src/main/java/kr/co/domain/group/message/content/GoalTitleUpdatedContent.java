package kr.co.domain.group.message.content;

import java.util.UUID;
import kr.co.domain.group.message.GroupMessageType;

public record GoalTitleUpdatedContent(UUID goalId, String goalTitle) implements
        GroupMessageContent {

    @Override
    public GroupMessageType getType() {
        return GroupMessageType.GOAL_TITLE_UPDATE;
    }

}