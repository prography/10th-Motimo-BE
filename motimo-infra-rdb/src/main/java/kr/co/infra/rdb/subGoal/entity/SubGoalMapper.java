package kr.co.infra.rdb.subGoal.entity;

import kr.co.domain.subGoal.SubGoal;
import kr.co.infra.rdb.goal.entity.GoalEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SubGoalMapper {

    public static SubGoal toDomain(SubGoalEntity entity) {

        return SubGoal.builder()
                .id(entity.getId())
                .goalId(entity.getGoal().getId())
                .userId(entity.getUserId())
                .title(entity.getTitle())
                .importance(entity.getImportance())
                .completed(entity.isCompleted())
                .completedChangedAt(entity.getCompletedChangedAt()).build();
    }

    public static SubGoalEntity toEntity(GoalEntity goal, SubGoal subGoal) {
        return new SubGoalEntity(
                goal,
                subGoal.getId(),
                subGoal.getTitle(),
                subGoal.getImportance()
        );
    }
}
