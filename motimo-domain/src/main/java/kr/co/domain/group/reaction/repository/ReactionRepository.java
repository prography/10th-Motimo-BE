package kr.co.domain.group.reaction.repository;

import java.util.Optional;
import java.util.UUID;
import kr.co.domain.group.reaction.Reaction;
import kr.co.domain.group.reaction.ReactionDomainId;

public interface ReactionRepository {
    Optional<Reaction> findById(ReactionDomainId id);

    Reaction upsert(Reaction reaction);

    void deleteByUserIdAndMessageId(UUID userId, UUID messageId);

    boolean existsById(ReactionDomainId id);
}
