package kr.co.infra.rdb.outbox.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.infra.rdb.common.uuid.GeneratedUuidV7Value;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "outbox_event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxEventEntity {

    @Id
    @GeneratedUuidV7Value
    private UUID id;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String payload;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public OutboxEventEntity(String eventType, String payload) {
        this.eventType = eventType;
        this.payload = payload;
    }
}
