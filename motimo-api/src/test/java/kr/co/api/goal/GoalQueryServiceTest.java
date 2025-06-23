package kr.co.api.goal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import kr.co.api.goal.dto.GoalWithSubGoalTodoDto;
import kr.co.api.goal.service.GoalQueryService;
import kr.co.api.todo.service.TodoQueryService;
import kr.co.domain.goal.DueDate;
import kr.co.domain.goal.Goal;
import kr.co.domain.goal.repository.GoalRepository;
import kr.co.domain.subGoal.SubGoal;
import kr.co.domain.todo.TodoStatus;
import kr.co.domain.todo.dto.TodoSummary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Goal Service Command 테스트")
class GoalQueryServiceTest {

    @Mock
    private GoalRepository goalRepository;
    @Mock
    private TodoQueryService todoQueryService;

    @InjectMocks
    private GoalQueryService goalQueryService;

    private Goal createMockGoal(UUID userId, UUID goalId, List<SubGoal> subGoals) {
        final DueDate dueDate = DueDate.of(2);
        return Goal.builder().id(goalId).userId(userId).title("goalTitle").dueDate(dueDate)
                .createdAt(LocalDateTime.of(2025, 6, 19, 4, 40))
                .updatedAt(LocalDateTime.now())
                .subGoals(subGoals).build();
    }

    private List<TodoSummary> createMockTodos(int count) {
        List<TodoSummary> summaries = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            summaries.add(new TodoSummary(
                    UUID.randomUUID(),
                    "테스트 투두",
                    LocalDate.now(),
                    TodoStatus.INCOMPLETE,
                    LocalDateTime.now(),
                    false));
        }
        return summaries;
    }

    @Nested
    @DisplayName("목표 세부목표, 투두 목록 조회 테스트")
    class GoalReadListTest {

        @Test
        void 목표_세부목표_투두_목록_조회() {
            final UUID userId = UUID.randomUUID();
            final UUID goalId = UUID.randomUUID();
            final List<SubGoal> subGoals = List.of(
                    SubGoal.builder().id(UUID.randomUUID()).title("sub goal title").importance(1)
                            .completed(false).build(),
                    SubGoal.builder().id(UUID.randomUUID()).title("sub goal title2").importance(1)
                            .completed(false).build(),
                    SubGoal.builder().id(UUID.randomUUID()).title("sub goal title3").importance(2)
                            .completed(false).build(),
                    SubGoal.builder().id(UUID.randomUUID()).title("sub goal title4").importance(3)
                            .completed(false).build(),
                    SubGoal.builder().id(UUID.randomUUID()).title("sub goal title5").importance(1)
                            .completed(false).build()
            );

            // given
            List<TodoSummary> mockTodos = createMockTodos(3);
            Goal mockGoal = createMockGoal(userId, goalId, subGoals);
            givenGoalWithSubGoalsAndTodos(goalId, mockGoal, mockTodos);

            // when
            GoalWithSubGoalTodoDto dto = goalQueryService.getGoalWithSubGoalTodayTodos(goalId);

            // then
            assertThat(dto).isNotNull();
            assertThat(dto.subGoals()).isNotEmpty();
            subGoalsSortTest(dto, subGoals);
            dto.subGoals().forEach(subGoalDto ->
                    assertThat(subGoalDto.todos()).hasSize(3)
            );
            assertThat(mockGoal.getUserId()).isEqualTo(userId);
        }

        private void subGoalsSortTest(GoalWithSubGoalTodoDto dto, List<SubGoal> subGoals) {
            List<Integer> importanceList = dto.subGoals().stream()
                    .map(subGoal -> subGoals.stream()
                            .filter(sg -> sg.getId().equals(subGoal.id()))
                            .findFirst()
                            .orElseThrow()
                            .getImportance())
                    .toList();
            List<Integer> sortedImportanceList = new ArrayList<>(importanceList);
            sortedImportanceList.sort(Comparator.naturalOrder());
            assertThat(importanceList).isEqualTo(sortedImportanceList);
        }

        @Test
        void 세부목표가_없을_경우_목표_세부목표_투두_목록_조회() {
            final UUID userId = UUID.randomUUID();
            final UUID goalId = UUID.randomUUID();

            // given
            List<TodoSummary> mockTodos = createMockTodos(3);
            Goal mockGoal = createMockGoal(userId, goalId, List.of());
            givenGoalWithSubGoalsAndTodos(goalId, mockGoal, mockTodos);

            // when
            GoalWithSubGoalTodoDto dto = goalQueryService.getGoalWithSubGoalTodayTodos(goalId);

            // then
            assertThat(dto).isNotNull();
            assertThat(dto.subGoals()).isEmpty();
        }

        private void givenGoalWithSubGoalsAndTodos(UUID goalId, Goal mockGoal,
                List<TodoSummary> mockTodos) {
            when(goalRepository.findById(goalId)).thenReturn(mockGoal);

            mockGoal.getSubGoals().forEach(subGoal ->
                    when(todoQueryService.getIncompleteOrTodayTodosBySubGoalId(subGoal.getId()))
                            .thenReturn(mockTodos)
            );
        }

        @Test
        void 완료한_세부목표의_경우_조회되지_않는다() {
            final UUID userId = UUID.randomUUID();
            final UUID goalId = UUID.randomUUID();
            final List<SubGoal> subGoals = List.of(
                    SubGoal.builder().id(UUID.randomUUID()).title("sub goal title").importance(1)
                            .completed(true).build(),
                    SubGoal.builder().id(UUID.randomUUID()).title("sub goal title2").importance(1)
                            .completed(true).build(),
                    SubGoal.builder().id(UUID.randomUUID()).title("sub goal title3").importance(2)
                            .completed(false).build(),
                    SubGoal.builder().id(UUID.randomUUID()).title("sub goal title4").importance(3)
                            .completed(false).build(),
                    SubGoal.builder().id(UUID.randomUUID()).title("sub goal title5").importance(1)
                            .completed(false).build()
            );

            // given
            Goal mockGoal = createMockGoal(userId, goalId, subGoals);
            when(goalRepository.findById(goalId)).thenReturn(mockGoal);

            // when
            GoalWithSubGoalTodoDto dto = goalQueryService.getGoalWithSubGoalTodayTodos(goalId);

            // then
            assertThat(dto).isNotNull();
            assertThat(dto.subGoals()).hasSize(3);
        }


    }
}