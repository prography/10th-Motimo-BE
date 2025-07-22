package kr.co.domain.common.event.group.message;

import java.util.UUID;
import kr.co.domain.group.reaction.ReactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TodoReactionFirstCreatedEvent {
    public UUID userId;
    public ReactionType reactionType;
    public UUID todoId;

    // MessageReactionFirstCreatedEvent로 공통화 할까 하였으나 현재 '투두'에 반응을 남긴다는 워딩을 쓰고 있기에 TodoReaction 네이밍 사용
    public TodoReactionFirstCreatedEvent(UUID userId, ReactionType reactionType, UUID todoId) {
        this.userId = userId;
        this.reactionType = reactionType;
        this.todoId = todoId;
    }
}
