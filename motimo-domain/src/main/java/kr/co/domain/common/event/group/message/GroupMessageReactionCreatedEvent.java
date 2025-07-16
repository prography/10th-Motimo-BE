package kr.co.domain.common.event.group.message;

import java.util.UUID;
import kr.co.domain.common.event.Event;
import kr.co.domain.group.reaction.ReactionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupMessageReactionCreatedEvent extends Event {

    private UUID userId;
    private UUID messageId;
    private ReactionType reactionType;

    public static GroupMessageReactionCreatedEvent of(UUID userId, UUID messageId,
            ReactionType reactionType) {
        return new GroupMessageReactionCreatedEvent(
                userId, messageId, reactionType
        );
    }
}
