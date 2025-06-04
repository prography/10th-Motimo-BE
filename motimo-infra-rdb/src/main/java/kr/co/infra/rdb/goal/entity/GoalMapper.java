package kr.co.infra.rdb.goal.entity;

import java.util.List;
import java.util.stream.Collectors;
import kr.co.domain.goal.Goal;
import kr.co.infra.rdb.subGoal.entity.SubGoalEntity;
import kr.co.infra.rdb.subGoal.entity.SubGoalMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GoalMapper {

    public static Goal toDomain(GoalEntity entity) {

        return Goal.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .title(entity.getTitle())
                .dueDate(entity.getDueDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .completed(entity.isCompleted())
                .completedAt(entity.getCompletedAt())
                .subGoals(null).build();
    }

    public static GoalEntity toEntity(Goal goal) {
        List<SubGoalEntity> subGoalEntities = goal.getSubGoals().stream()
                .map(subGoal -> SubGoalMapper.toEntity(goal.getUserId(), subGoal))
                .collect(Collectors.toList());

        return new GoalEntity(
                goal.getUserId(),
                goal.getTitle(),
                goal.getDueDate(),
                subGoalEntities
        );
    }
}
