package kr.co.domain.group.message.content;

import java.util.UUID;
import kr.co.domain.group.message.GroupMessageType;
import kr.co.domain.group.reaction.ReactionType;

public record MessageReactionContent(UUID referenceMessageId, ReactionType reactionType) implements GroupMessageContent {

    @Override
    public GroupMessageType getType() {
        return GroupMessageType.MESSAGE_REACTION;
    }
}
