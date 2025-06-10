package kr.co.infra.rdb.goal.entity;

import kr.co.domain.goal.Goal;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GoalMapper {

    public static Goal toDomain(GoalEntity entity) {

        return Goal.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .title(entity.getTitle())
                .dueDate(entity.getDueDate().toDomain())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .completed(entity.isCompleted())
                .completedAt(entity.getCompletedAt())
                .subGoals(null).build();
    }

    public static GoalEntity toEntity(Goal goal) {
        return new GoalEntity(
                goal.getId(),
                goal.getUserId(),
                goal.getTitle(),
                DueDateEmbeddable.from(goal.getDueDate()),
                goal.isCompleted()
        );
    }
}
