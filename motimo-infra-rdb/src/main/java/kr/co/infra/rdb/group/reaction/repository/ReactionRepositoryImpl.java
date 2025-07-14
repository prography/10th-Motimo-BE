package kr.co.infra.rdb.group.reaction.repository;

import java.util.Optional;
import java.util.UUID;
import kr.co.domain.group.reaction.Reaction;
import kr.co.domain.group.reaction.repository.ReactionRepository;
import kr.co.infra.rdb.group.reaction.ReactionEntity;
import kr.co.infra.rdb.group.reaction.ReactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReactionRepositoryImpl implements ReactionRepository {

    private final ReactionJpaRepository reactionJpaRepository;

    public Optional<Reaction> findByUserIdAndMessageId(UUID userId, UUID messageId) {
        Optional<ReactionEntity> reactionEntity = reactionJpaRepository.findByUserIdAndMessageId(userId, messageId);

        return reactionEntity.map(ReactionMapper::toDomain);
    }

    @Override
    public Reaction upsert(Reaction reaction) {
        ReactionEntity reactionEntity = reactionJpaRepository.save(
                ReactionMapper.toEntity(reaction));
        return ReactionMapper.toDomain(reactionEntity);
    }

    @Override
    public void deleteByUserIdAndMessageId(UUID userId, UUID messageId) {
        reactionJpaRepository.deleteByUserIdAndMessageId(userId, messageId);
    }
}
