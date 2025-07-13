package kr.co.infra.rdb.group.reaction.repository;

import java.util.UUID;
import kr.co.infra.rdb.group.reaction.ReactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionJpaRepository extends JpaRepository<ReactionEntity, UUID> {
    void deleteByUserIdAndMessageId(UUID userId, UUID messageId);
}
