package kr.co.domain.group.message.strategy;

import java.util.UUID;
import kr.co.domain.group.message.GroupMessage;
import kr.co.domain.group.message.GroupMessageType;
import kr.co.domain.group.message.content.GroupMessageContent;
import kr.co.domain.group.message.content.MessageReactionContent;
import kr.co.domain.group.message.frozenData.ReactionFrozenData;
import kr.co.domain.group.reaction.ReactionType;
import org.springframework.stereotype.Component;

@Component
public class MessageReactionMessageStrategy implements MessageContentStrategy {

    @Override
    public GroupMessageContent createContent(GroupMessage message, MessageContentData contentData) {
        UUID referenceMessageId = message.getMessageReference().referenceId();
        ReactionType reactionType = message.getFrozenData(ReactionFrozenData.class).getReactionType();

        return new MessageReactionContent(referenceMessageId, reactionType);
    }

    @Override
    public GroupMessageType getMessageType() {
        return GroupMessageType.MESSAGE_REACTION;
    }
}
