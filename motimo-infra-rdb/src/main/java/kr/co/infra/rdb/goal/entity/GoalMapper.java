package kr.co.infra.rdb.goal.entity;

import java.util.List;
import kr.co.domain.goal.Goal;
import kr.co.domain.subGoal.SubGoal;
import kr.co.infra.rdb.subGoal.entity.SubGoalMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GoalMapper {

    public static Goal toDomain(GoalEntity entity) {
        List<SubGoal> subGoals = entity.getSubGoals()
                .stream()
                .map(SubGoalMapper::toDomain)
                .toList();

        return Goal.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .title(entity.getTitle())
                .dueDate(entity.getDueDate().toDomain())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .completed(entity.isCompleted())
                .completedAt(entity.getCompletedAt())
                .subGoals(subGoals)
                .groupId(entity.getGroup().getId())
                .build();
    }

    public static Goal toDomainWithoutSubGoal(GoalEntity entity) {
        return Goal.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .title(entity.getTitle())
                .dueDate(entity.getDueDate().toDomain())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .completed(entity.isCompleted())
                .completedAt(entity.getCompletedAt())
                .subGoals(null)
                .groupId(entity.getGroup().getId())
                .build();
    }

    public static GoalEntity toEntity(Goal goal) {
        GoalEntity goalEntity = new GoalEntity(
                goal.getId(),
                goal.getUserId(),
                goal.getTitle(),
                DueDateEmbeddable.from(goal.getDueDate()),
                goal.isCompleted()
        );

        goalEntity.addSubGoals(
                goal.getSubGoals().stream().map(s -> SubGoalMapper.toEntity(goalEntity, s))
                        .toList());

        return goalEntity;
    }
}
