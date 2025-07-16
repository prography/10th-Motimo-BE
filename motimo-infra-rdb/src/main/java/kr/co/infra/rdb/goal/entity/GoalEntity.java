package kr.co.infra.rdb.goal.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import kr.co.infra.rdb.common.entity.BaseEntity;
import kr.co.infra.rdb.common.uuid.GeneratedUuidV7Value;
import kr.co.infra.rdb.subGoal.entity.SubGoalEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "goals")
@SoftDelete(columnName = "is_deleted")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoalEntity extends BaseEntity {

    @Id
    @GeneratedUuidV7Value
    private UUID id;

    private UUID userId;

    private String title;

    @Embedded
    private DueDateEmbeddable dueDate;

    private boolean completed;

    private LocalDateTime completedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "goal", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<SubGoalEntity> subGoals = new ArrayList<>();

    private UUID groupId = null;

    public GoalEntity(UUID id, UUID userId, String title, DueDateEmbeddable dueDate, boolean completed) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    public void addSubGoals(List<SubGoalEntity> subGoals) {
        this.subGoals.addAll(subGoals);
    }

    public void updateSubGoals(List<SubGoalEntity> subGoals) {
        this.subGoals.clear();
        this.subGoals.addAll(subGoals);
    }

    public void update(String title, DueDateEmbeddable dueDate, boolean completed, LocalDateTime completedAt) {
        this.title = title;
        this.dueDate = dueDate;
        this.completed = completed;
        this.completedAt = completedAt;
    }

    public void connectGroup(UUID groupId) {
        this.groupId = groupId;
    }

}