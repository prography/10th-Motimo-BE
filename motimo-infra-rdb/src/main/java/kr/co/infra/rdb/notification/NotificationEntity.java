package kr.co.infra.rdb.notification;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import kr.co.domain.notification.NotificationType;
import kr.co.infra.rdb.common.entity.BaseEntity;
import kr.co.infra.rdb.common.uuid.GeneratedUuidV7Value;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "notifications")
@SoftDelete(columnName = "is_deleted") // 필요할까?
@Getter
@NoArgsConstructor
public class NotificationEntity extends BaseEntity {
    @Id
    @GeneratedUuidV7Value
    private UUID id;

    private UUID senderId;

    private UUID receiverId;

    @Deprecated
    private String title;

    @Deprecated
    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private UUID referenceId;

    private boolean isRead = false;

    public NotificationEntity(UUID senderId, UUID receiverId, NotificationType type, UUID referenceId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.type = type;
        this.referenceId = referenceId;
    }

    public void readComplete() {
        isRead = true;
    }
}
