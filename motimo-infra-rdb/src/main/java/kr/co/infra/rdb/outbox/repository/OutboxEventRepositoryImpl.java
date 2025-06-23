package kr.co.infra.rdb.outbox.repository;

import java.util.List;
import java.util.UUID;
import kr.co.domain.common.outbox.OutboxEvent;
import kr.co.domain.common.outbox.OutboxEventRepository;
import kr.co.infra.rdb.outbox.entity.OutboxEventEntity;
import kr.co.infra.rdb.outbox.util.OutboxEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OutboxEventRepositoryImpl implements OutboxEventRepository {

    private final OutboxEventJpaRepository outboxEventJpaRepository;

    @Override
    public OutboxEvent create(OutboxEvent event) {
        OutboxEventEntity entity = OutboxEventMapper.toEntity(event);
        return OutboxEventMapper.toDomain(outboxEventJpaRepository.save(entity));
    }

    @Override
    public List<OutboxEvent> findTop100ByOrderByCreatedAtAsc() {
        return outboxEventJpaRepository.findTop100ByOrderByCreatedAtAsc()
                .stream()
                .map(OutboxEventMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        outboxEventJpaRepository.deleteById(id);
    }
}
