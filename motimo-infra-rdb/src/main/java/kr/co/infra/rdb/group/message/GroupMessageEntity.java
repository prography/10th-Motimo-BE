package kr.co.infra.rdb.group.message;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.co.domain.group.message.GroupMessageType;
import kr.co.infra.rdb.common.uuid.GeneratedUuidV7Value;
import kr.co.infra.rdb.group.reaction.ReactionEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "group_message")
@SoftDelete(columnName = "is_deleted")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupMessageEntity {

    @Id
    @GeneratedUuidV7Value
    @Column(name = "id")
    private UUID id;

    private UUID groupId;

    private UUID senderId;

    private String senderName;

    private GroupMessageType type;

    private UUID targetId; // nullable

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group_message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReactionEntity> reactions;

    @CreatedDate
    private LocalDateTime sendAt;
}
