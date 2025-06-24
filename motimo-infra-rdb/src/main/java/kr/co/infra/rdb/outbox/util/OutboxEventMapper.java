package kr.co.infra.rdb.outbox.util;

import kr.co.domain.common.outbox.OutboxEvent;
import kr.co.infra.rdb.outbox.entity.OutboxEventEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OutboxEventMapper {

    public static OutboxEvent toDomain(OutboxEventEntity entity) {
        return OutboxEvent.builder()
                .id(entity.getId())
                .eventType(entity.getEventType())
                .payload(entity.getPayload())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static OutboxEventEntity toEntity(OutboxEvent domain) {
        return new OutboxEventEntity(domain.getEventType(), domain.getPayload());
    }

}
