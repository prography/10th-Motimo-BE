package kr.co.domain.group.reaction.repository;

import java.util.Optional;
import java.util.UUID;
import kr.co.domain.group.reaction.Reaction;

public interface ReactionRepository {
    Optional<Reaction> findByUserIdAndMessageId(UUID userId, UUID messageId);

    Reaction upsert(Reaction reaction);

    void delete(UUID reactionId);

    void deleteByUserIdAndMessageId(UUID userId, UUID messageId);
}
