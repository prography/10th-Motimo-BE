package kr.co.infra.rdb.goal.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.UUID;
import kr.co.domain.goal.DueDate;
import kr.co.domain.goal.Goal;
import kr.co.domain.subGoal.SubGoal;
import kr.co.infra.rdb.goal.entity.DueDateEmbeddable;
import kr.co.infra.rdb.goal.entity.GoalEntity;
import kr.co.infra.rdb.goal.entity.GoalMapper;
import kr.co.infra.rdb.subGoal.entity.SubGoalEntity;
import kr.co.infra.rdb.subGoal.entity.SubGoalMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("GoalMapper/SubGoalMapper toEntity() 테스트")
class GoalMapperTest {

    @Test
    void Goal을_GoalEntity로_변환() {
        UUID userId = UUID.randomUUID();

        Goal goal = Goal.createGoal()
                .userId(userId)
                .title("goal title")
                .dueDate(DueDate.of(3))
                .subGoals(new ArrayList<>())
                .build();

        GoalEntity entity = GoalMapper.toEntity(goal);

        assertThat(entity).isNotNull();
        assertThat(entity.getUserId()).isEqualTo(goal.getUserId());
        assertThat(entity.getTitle()).isEqualTo(goal.getTitle());
        assertThat(entity.getDueDate()).isInstanceOf(DueDateEmbeddable.class);
        assertThat(entity.isCompleted()).isFalse();
        assertThat(entity.getCompletedAt()).isNull();
    }

    @Test
    void SubGoal을_SubGoalEntity로_변환() {
        UUID userId = UUID.randomUUID();

        Goal goal = Goal.createGoal()
                .userId(userId)
                .title("goal title")
                .dueDate(DueDate.of(3))
                .subGoals(new ArrayList<>())
                .build();

        GoalEntity goalEntity = GoalMapper.toEntity(goal);

        SubGoal subGoal = SubGoal.createSubGoal()
                .title("goal title")
                .order(1)
                .build();

        SubGoalEntity entity = SubGoalMapper.toEntity(goalEntity, subGoal);

        assertThat(entity).isNotNull();
        assertThat(entity.getTitle()).isEqualTo(subGoal.getTitle());
        assertThat(entity.getImportance()).isEqualTo(subGoal.getOrder());
        assertThat(entity.isCompleted()).isFalse();
        assertThat(entity.getCompletedChangedAt()).isNull();
    }
}