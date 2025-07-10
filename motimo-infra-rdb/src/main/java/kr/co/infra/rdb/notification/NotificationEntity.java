package kr.co.infra.rdb.notification;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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

    private String title;

    private String content;

    private NotificationType type;

    private UUID referenceId;

    public NotificationEntity(UUID senderId, UUID receiverId, String title, String content, NotificationType type, UUID referenceId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.title = title;
        this.content = content;
        this.type = type;
        this.referenceId = referenceId;
    }
}
