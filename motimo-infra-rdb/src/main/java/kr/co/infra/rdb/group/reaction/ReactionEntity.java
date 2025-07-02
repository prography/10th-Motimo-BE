package kr.co.infra.rdb.group.reaction;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.domain.group.reaction.ReactionType;
import kr.co.infra.rdb.group.message.GroupMessageEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "reaction")
@SoftDelete(columnName = "is_deleted")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReactionEntity {

    @Id
    @UuidGenerator
    private UUID id;

    private UUID senderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private GroupMessageEntity message;

    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;

    @CreatedDate
    private LocalDateTime createdAt;

    protected ReactionEntity(UUID id, UUID senderId, GroupMessageEntity message,
            ReactionType reactionType) {
        this.id = id;
        this.senderId = senderId;
        this.message = message;
        this.reactionType = reactionType;
    }
}
