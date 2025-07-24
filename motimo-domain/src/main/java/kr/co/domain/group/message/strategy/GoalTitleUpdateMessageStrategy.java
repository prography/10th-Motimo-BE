package kr.co.domain.group.message.strategy;

import java.util.UUID;
import kr.co.domain.group.message.GroupMessage;
import kr.co.domain.group.message.GroupMessageType;
import kr.co.domain.group.message.content.GoalTitleUpdatedContent;
import kr.co.domain.group.message.content.GroupMessageContent;
import kr.co.domain.group.message.frozenData.GoalTitleFrozenData;
import org.springframework.stereotype.Component;

@Component
public class GoalTitleUpdateMessageStrategy implements MessageContentStrategy {

    @Override
    public GroupMessageContent createContent(GroupMessage message, MessageContentData contentData) {
        UUID goalId = message.getMessageReference().referenceId();
        String goalTitle = message.getFrozenData(GoalTitleFrozenData.class).getTitle();
        return new GoalTitleUpdatedContent(goalId, goalTitle);
    }

    @Override
    public GroupMessageType getMessageType() {
        return GroupMessageType.GOAL_TITLE_UPDATE;
    }
}
