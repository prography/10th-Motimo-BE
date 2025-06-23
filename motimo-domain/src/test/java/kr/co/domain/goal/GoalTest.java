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
    class ProgressTest {

        @Test
        void 진행률_세부목표가_없으면_0퍼센트() {
            Goal goal = getGoalWithoutSubGoals();
            goal.addSubGoals(List.of());

            float progress = goal.calculateProgress();

            assertEquals(0f, progress);
        }

        @Test
        void 진행률_모두_미완료면_0퍼센트() {
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
        void 진행률_모두완료면_100퍼센트() {
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
        void 진행률_일부완료_정확한퍼센트계산() {
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
