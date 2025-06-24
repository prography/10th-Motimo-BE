package kr.co.domain.goal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import kr.co.domain.subGoal.SubGoal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Goal 도메인 테스트")
public class GoalTest {

    @Nested
    @DisplayName("목표 진행률 계산 테스트")
    class ProgressTest {

        @Test
        void 진행률_세부목표가_없으면_0_퍼센트() {
            Goal goal = getGoalWithoutSubGoals();
            goal.addSubGoals(List.of());

            float progress = goal.calculateProgress();

            assertEquals(0f, progress);
        }

        @Test
        void 진행률_모두_미완료시_0_퍼센트() {
            Goal goal = getGoalWithoutSubGoals();
            goal.addSubGoals(List.of(
                    SubGoal.builder().completed(false).build(),
                    SubGoal.builder().completed(false).build(),
                    SubGoal.builder().completed(false).build()
            ));

            float progress = goal.calculateProgress();

            assertEquals(0f, progress);
        }

        @Test
        void 진행률_모두_완료시_100_퍼센트() {
            Goal goal = getGoalWithoutSubGoals();
            goal.addSubGoals(List.of(
                    SubGoal.builder().completed(true).build(),
                    SubGoal.builder().completed(true).build(),
                    SubGoal.builder().completed(true).build()
            ));

            float progress = goal.calculateProgress();

            assertEquals(100f, progress);
        }

        @Test
        void 진행률_일부완료시_정확한_퍼센트_계산() {
            Goal goal = getGoalWithoutSubGoals();
            goal.addSubGoals(List.of(
                    SubGoal.builder().completed(false).build(),
                    SubGoal.builder().completed(false).build(),
                    SubGoal.builder().completed(true).build()
            ));

            float progress = goal.calculateProgress();
            float correctProgress = ((float) 1 / 3) * 100;

            assertEquals(correctProgress, progress);
        }

        private Goal getGoalWithoutSubGoals() {
            return Goal.builder().build();
        }
    }

}
