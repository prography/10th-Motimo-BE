package kr.co.infra.rdb.group.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.infra.rdb.common.entity.BaseEntity;
import kr.co.infra.rdb.common.uuid.GeneratedUuidV7Value;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "group_members")
@SoftDelete(columnName = "is_deleted")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupMemberEntity extends BaseEntity {
    @Id
    @GeneratedUuidV7Value
    private UUID id;

    private UUID userId;

    private UUID goalId;

    private UUID groupId;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime joinedDate;

    @ColumnDefault(value = "true")
    private boolean isNotificationActive;

    public GroupMemberEntity(UUID userId, UUID goalId, GroupEntity group, LocalDateTime joinedDate) {
        this.userId = userId;
        this.goalId = goalId;
        this.groupId = group.getId();
        this.joinedDate = joinedDate;
    }
}
