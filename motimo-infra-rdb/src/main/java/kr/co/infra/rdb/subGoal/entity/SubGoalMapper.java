package kr.co.infra.rdb.subGoal.entity;

import java.util.UUID;
import kr.co.domain.subGoal.SubGoal;
import kr.co.infra.rdb.goal.entity.GoalEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SubGoalMapper {

    public static SubGoal toDomain(SubGoalEntity entity) {

        return SubGoal.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .importance(entity.getImportance())
                .completed(entity.isCompleted())
                .completedChangedAt(entity.getCompletedChangedAt()).build();
    }

    public static SubGoalEntity toEntity(UUID userId, GoalEntity goal, SubGoal subGoal) {

        return new SubGoalEntity(
                userId,
                goal,
                subGoal.getTitle(),
                subGoal.getImportance()
        );
    }
}
