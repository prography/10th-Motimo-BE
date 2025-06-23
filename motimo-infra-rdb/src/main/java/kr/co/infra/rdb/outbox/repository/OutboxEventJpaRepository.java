package kr.co.infra.rdb.outbox.repository;

import java.util.List;
import java.util.UUID;
import kr.co.infra.rdb.outbox.entity.OutboxEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEventJpaRepository extends JpaRepository<OutboxEventEntity, UUID> {

    List<OutboxEventEntity> findTop100ByOrderByCreatedAtAsc();
}
