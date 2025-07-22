package kr.co.infra.rdb.group.reaction.repository;

import java.util.Optional;
import java.util.UUID;
import kr.co.domain.group.reaction.Reaction;
import kr.co.domain.group.reaction.ReactionDomainId;
import kr.co.domain.group.reaction.repository.ReactionRepository;
import kr.co.infra.rdb.group.reaction.ReactionEntity;
import kr.co.infra.rdb.group.reaction.ReactionId;
import kr.co.infra.rdb.group.reaction.ReactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReactionRepositoryImpl implements ReactionRepository {

    private final ReactionJpaRepository reactionJpaRepository;

    public Optional<Reaction> findById(ReactionDomainId id) {
        Optional<ReactionEntity> reactionEntity = reactionJpaRepository.findById(ReactionId.from(id));

        return reactionEntity.map(ReactionMapper::toDomain);
    }

    @Override
    public Reaction upsert(Reaction reaction) {
        ReactionEntity reactionEntity = reactionJpaRepository.save(
                ReactionMapper.toEntity(reaction));
        return ReactionMapper.toDomain(reactionEntity);
    }

    public boolean existsById(ReactionDomainId id) {
        return reactionJpaRepository.existsById(ReactionId.from(id));
    }

    @Override
    public void deleteByUserIdAndMessageId(UUID userId, UUID messageId) {
        reactionJpaRepository.deleteByUserIdAndMessageId(userId, messageId);
    }
}
