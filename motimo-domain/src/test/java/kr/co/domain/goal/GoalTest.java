package kr.co.domain.goal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.TodoStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Goal 도메인 테스트")
public class GoalTest {

    @Nested
    @DisplayName("목표 진행률 계산 테스트")
    class ProgressTest {

        @Test
        void 진행률_할일이_없으면_0_퍼센트() {
            Goal goal = Goal.builder().build();

            float progress = goal.calculateProgress(List.of());

            assertEquals(0f, progress);
        }

        @Test
        void 진행률_모두_미완료시_0_퍼센트() {
            Goal goal = Goal.builder().build();

            List<Todo> todos = List.of(
                    Todo.builder().status(TodoStatus.INCOMPLETE).build(),
                    Todo.builder().status(TodoStatus.INCOMPLETE).build(),
                    Todo.builder().status(TodoStatus.INCOMPLETE).build()
            );

            float progress = goal.calculateProgress(todos);

            assertEquals(0f, progress);
        }

        @Test
        void 진행률_모두_완료시_100_퍼센트() {
            Goal goal = Goal.builder().build();

            List<Todo> todos = List.of(
                    Todo.builder().status(TodoStatus.COMPLETE).build(),
                    Todo.builder().status(TodoStatus.COMPLETE).build(),
                    Todo.builder().status(TodoStatus.COMPLETE).build()
            );

            float progress = goal.calculateProgress(todos);

            assertEquals(100f, progress);
        }

        @Test
        void 진행률_일부완료시_정확한_퍼센트_계산() {
            Goal goal = Goal.builder().build();

            List<Todo> todos = List.of(
                    Todo.builder().status(TodoStatus.INCOMPLETE).build(),
                    Todo.builder().status(TodoStatus.INCOMPLETE).build(),
                    Todo.builder().status(TodoStatus.COMPLETE).build()
            );

            float progress = goal.calculateProgress(todos);
            float correctProgress = ((float) 1 / 3) * 100;

            assertEquals(correctProgress, progress);
        }
    }
}
