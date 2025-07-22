package kr.co.domain.common.event.group.message;

import java.util.UUID;
import kr.co.domain.common.event.Event;
import kr.co.domain.group.reaction.ReactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageReactionFirstCreatedEvent extends Event {
    public UUID userId;
    public ReactionType reactionType;
    public UUID messageId;

    public MessageReactionFirstCreatedEvent(UUID userId, ReactionType reactionType, UUID messageId) {
        this.userId = userId;
        this.reactionType = reactionType;
        this.messageId = messageId;
    }
}
