package kr.co.infra.rdb.subGoal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.infra.rdb.common.uuid.GeneratedUuidV7Value;
import kr.co.infra.rdb.goal.entity.GoalEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sub_goals")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SubGoalEntity {

    @Id
    @GeneratedUuidV7Value
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private GoalEntity goal;

    private UUID userId;

    private String title;

    private int importance;

    private boolean completed = false;

    private LocalDateTime completedChangedAt;

    protected SubGoalEntity(UUID userId, String title, int importance) {
        this.userId = userId;
        this.title = title;
        this.importance = importance;
        this.completed = false;
    }
}
