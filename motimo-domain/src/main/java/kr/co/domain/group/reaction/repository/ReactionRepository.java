package kr.co.domain.group.reaction.repository;

import java.util.UUID;
import kr.co.domain.group.reaction.Reaction;

public interface ReactionRepository {

    Reaction create(Reaction reaction);

    void delete(UUID reactionId);

    void deleteByUserIdAndMessageId(UUID userId, UUID messageId);
}
