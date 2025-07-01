package kr.co.infra.rdb.subGoal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.infra.rdb.common.entity.BaseEntity;
import kr.co.infra.rdb.common.uuid.GeneratedUuidV7Value;
import kr.co.infra.rdb.goal.entity.GoalEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "sub_goals")
@SoftDelete(columnName = "is_deleted")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubGoalEntity extends BaseEntity {

    @Id
    @GeneratedUuidV7Value
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private GoalEntity goal;

    private UUID userId;

    private String title;

    private int importance; // TODO 추후 order로 변경

    private boolean completed = false;

    private LocalDateTime completedChangedAt;

    protected SubGoalEntity(GoalEntity goal, UUID id, String title, int order) {
        this.goal = goal;
        this.id = id;
        this.userId = goal.getUserId();
        this.title = title;
        this.importance = order;
        this.completed = false;
    }

    public void update(String title, int order, boolean completed) {
        this.title = title;
        this.importance = order;
        this.completed = completed;
        if(completed) {
            this.completedChangedAt = LocalDateTime.now();
        }
    }

    public void updateOrder(int order) {
        this.importance = order;
    }
}
