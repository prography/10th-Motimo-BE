package kr.co.domain.common.outbox;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository {

    OutboxEvent create(OutboxEvent event);

    List<OutboxEvent> findTop100ByOrderByCreatedAtAsc();

    void deleteById(UUID id);
}
