package kr.co.infra.rdb.goal.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import kr.co.infra.rdb.common.uuid.GeneratedUuidV7Value;
import kr.co.infra.rdb.subGoal.entity.SubGoalEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "goals")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoalEntity {

    @Id
    @GeneratedUuidV7Value
    private UUID id;

    private UUID userId;

    private String title;

    @Embedded
    private DueDateEmbeddable dueDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean completed;

    private LocalDateTime completedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "goal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubGoalEntity> subGoals = new ArrayList<>();

    protected GoalEntity(UUID userId, String title, DueDateEmbeddable dueDate, List<SubGoalEntity> subGoals) {
        this.userId = userId;
        this.title = title;
        this.dueDate = dueDate;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.completed = false;
        this.subGoals = subGoals;
    }
}