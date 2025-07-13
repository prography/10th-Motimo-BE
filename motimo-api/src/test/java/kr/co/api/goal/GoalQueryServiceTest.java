package kr.co.api.goal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import kr.co.api.goal.dto.CompletedGoalItemDto;
import kr.co.api.goal.dto.GoalWithSubGoalTodoDto;
import kr.co.api.goal.service.GoalQueryService;
import kr.co.api.todo.service.TodoQueryService;
import kr.co.domain.goal.DueDate;
import kr.co.domain.goal.Goal;
import kr.co.domain.goal.dto.GoalTodoCount;
import kr.co.domain.goal.repository.GoalRepository;
import kr.co.domain.subGoal.SubGoal;
import kr.co.domain.todo.Emotion;
import kr.co.domain.todo.TodoStatus;
import kr.co.domain.todo.dto.TodoItemDto;
import kr.co.domain.todo.dto.TodoResultItemDto;
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

    @Nested
    @DisplayName("목표 세부목표, 투두 목록 조회 테스트")
    class GoalReadListTest {

        @Test
        void 목표_세부목표_투두_목록_조회() {
            final UUID userId = UUID.randomUUID();
            final UUID goalId = UUID.randomUUID();
            final List<SubGoal> subGoals = List.of(
                    SubGoal.builder().id(UUID.randomUUID()).title("sub goal title").order(1)
                            .completed(false).build(),
                    SubGoal.builder().id(UUID.randomUUID()).title("sub goal title2").order(1)
                            .completed(false).build(),
                    SubGoal.builder().id(UUID.randomUUID()).title("sub goal title3").order(2)
                            .completed(false).build(),
                    SubGoal.builder().id(UUID.randomUUID()).title("sub goal title4").order(3)
                            .completed(false).build(),
                    SubGoal.builder().id(UUID.randomUUID()).title("sub goal title5").order(1)
                            .completed(false).build()
            );

            // given
            List<TodoItemDto> mockTodos = createMockTodoItems(3);
            Goal mockGoal = createMockGoal(userId, goalId, subGoals);
            givenGoalWithSubGoalsAndTodos(goalId, mockGoal, mockTodos);

            // when
            GoalWithSubGoalTodoDto dto = goalQueryService.getGoalWithIncompleteSubGoalTodayTodos(
                    goalId);

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
                            .getOrder())
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
            List<TodoItemDto> mockTodos = createMockTodoItems(3);
            Goal mockGoal = createMockGoal(userId, goalId, List.of());
            givenGoalWithSubGoalsAndTodos(goalId, mockGoal, mockTodos);

            // when
            GoalWithSubGoalTodoDto dto = goalQueryService.getGoalWithIncompleteSubGoalTodayTodos(
                    goalId);

            // then
            assertThat(dto).isNotNull();
            assertThat(dto.subGoals()).isEmpty();
        }

        private void givenGoalWithSubGoalsAndTodos(UUID goalId, Goal mockGoal,
                List<TodoItemDto> mockTodos) {
            when(goalRepository.findById(goalId)).thenReturn(mockGoal);

            mockGoal.getSubGoals().forEach(subGoal ->
                    when(todoQueryService.getIncompleteOrTodayTodosBySubGoalId(subGoal.getId()))
                            .thenReturn(mockTodos)
            );
        }


    }

    @Nested
    @DisplayName("유저의 완료된 목표 조회 테스트")
    class GetCompletedGoalsByUserIdTest {

        private UUID userId = UUID.randomUUID();

        @Test
        void 유저_ID로_완료된_목표_리스트_조회() {
            // given
            List<Goal> completedGoals = createMockGoals(2);
            List<UUID> goalIds = completedGoals.stream().map(Goal::getId).toList();
            List<GoalTodoCount> goalTodoCounts = Arrays.asList(
                    new GoalTodoCount(goalIds.get(0), 5L, 3L),
                    new GoalTodoCount(goalIds.get(1), 3L, 2L)
            );

            when(goalRepository.findCompletedGoalsByUserId(userId)).thenReturn(completedGoals);
            when(todoQueryService.getTodoCountsByGoalIds(goalIds)).thenReturn(goalTodoCounts);

            // when
            List<CompletedGoalItemDto> result = goalQueryService.getCompletedGoalsByUserId(userId);

            // then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).todoCount()).isEqualTo(5L);
            assertThat(result.get(0).todoResultCount()).isEqualTo(3L);
            assertThat(result.get(1).todoCount()).isEqualTo(3L);
            assertThat(result.get(1).todoResultCount()).isEqualTo(2L);

            verify(goalRepository).findCompletedGoalsByUserId(userId);
            verify(todoQueryService).getTodoCountsByGoalIds(goalIds);
        }

        @Test
        void 완료된_목표가_없는_경우_빈_리스트_반환() {
            // given
            when(goalRepository.findCompletedGoalsByUserId(userId)).thenReturn(
                    Collections.emptyList());

            // when
            List<CompletedGoalItemDto> result = goalQueryService.getCompletedGoalsByUserId(userId);

            // then
            assertThat(result).isEmpty();
            verify(goalRepository).findCompletedGoalsByUserId(userId);
            verify(todoQueryService, never()).getTodoCountsByGoalIds(anyList());
        }

        @Test
        void 투두_카운트_정보가_없는_목표의_경우_기본값_0으로_설정() {
            // given
            List<Goal> completedGoals = createMockGoals(2);
            List<UUID> goalIds = completedGoals.stream().map(Goal::getId).toList();
            List<GoalTodoCount> goalTodoCounts = List.of(
                    new GoalTodoCount(goalIds.getFirst(), 5L, 3L)
            );

            when(goalRepository.findCompletedGoalsByUserId(userId)).thenReturn(completedGoals);
            when(todoQueryService.getTodoCountsByGoalIds(goalIds)).thenReturn(goalTodoCounts);

            // when
            List<CompletedGoalItemDto> result = goalQueryService.getCompletedGoalsByUserId(userId);

            // then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).todoCount()).isEqualTo(5L);
            assertThat(result.get(0).todoResultCount()).isEqualTo(3L);

            assertThat(result.get(1).todoCount()).isEqualTo(0L);
            assertThat(result.get(1).todoResultCount()).isEqualTo(0L);
        }
    }

    @Nested
    @DisplayName("목표의 모든 세부목표, 투두 조회 테스트")
    class GetGoalWithSubGoalAndTodosTest {

        private UUID goalId = UUID.randomUUID();

        @Test
        void 목표_ID로_목표와_세부목표_투두_조회() {
            // given
            Goal goal = createMockGoal(goalId);
            List<SubGoal> subGoals = createMockSubGoals();

            List<TodoItemDto> todos1 = createMockTodoItems(2);
            List<TodoItemDto> todos2 = createMockTodoItems(3);
            List<TodoItemDto> todos3 = createMockTodoItems(0);

            when(goalRepository.findById(goalId)).thenReturn(goal);
            when(goal.getSubGoals()).thenReturn(subGoals);
            when(todoQueryService.getTodosBySubGoalId(subGoals.get(0).getId())).thenReturn(todos1);
            when(todoQueryService.getTodosBySubGoalId(subGoals.get(1).getId())).thenReturn(todos2);
            when(todoQueryService.getTodosBySubGoalId(subGoals.get(2).getId())).thenReturn(todos3);

            // when
            GoalWithSubGoalTodoDto result = goalQueryService.getGoalWithSubGoalAndTodos(goalId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.subGoals()).hasSize(3);

            assertThat(result.subGoals().get(0).todos()).hasSize(3);
            assertThat(result.subGoals().get(1).todos()).hasSize(0);
            assertThat(result.subGoals().get(2).todos()).hasSize(2);

            verify(goalRepository).findById(goalId);
            verify(todoQueryService).getTodosBySubGoalId(subGoals.get(0).getId());
            verify(todoQueryService).getTodosBySubGoalId(subGoals.get(1).getId());
            verify(todoQueryService).getTodosBySubGoalId(subGoals.get(2).getId());
        }

        @Test
        void 세부목표가_순서대로_정렬되어_반환된다() {
            // given
            Goal goal = createMockGoal(goalId);
            List<SubGoal> subGoals = createMockSubGoals();

            List<TodoItemDto> todos = createMockTodoItems(1);

            when(goalRepository.findById(goalId)).thenReturn(goal);
            when(goal.getSubGoals()).thenReturn(subGoals);
            when(todoQueryService.getTodosBySubGoalId(any())).thenReturn(todos);

            // when
            GoalWithSubGoalTodoDto result = goalQueryService.getGoalWithSubGoalAndTodos(goalId);

            // then
            assertThat(result.subGoals()).hasSize(3);
            // 순서대로 정렬되어 있는지 확인
            assertThat(result.subGoals().get(0).title()).isEqualTo("세부목표 1");
            assertThat(result.subGoals().get(1).title()).isEqualTo("세부목표 2");
            assertThat(result.subGoals().get(2).title()).isEqualTo("세부목표 3");
        }

        @Test
        void 세부목표가_없는_경우_빈_리스트_반환() {
            // given
            Goal goal = createMockGoal(goalId);
            goal.addSubGoals(Collections.emptyList());

            when(goalRepository.findById(goalId)).thenReturn(goal);

            // when
            GoalWithSubGoalTodoDto result = goalQueryService.getGoalWithSubGoalAndTodos(goalId);

            // then
            assertThat(result.subGoals()).isEmpty();
            verify(todoQueryService, never()).getTodosBySubGoalId(any());
        }
    }

    private List<Goal> createMockGoals(int count) {
        List<Goal> goals = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Goal goal = createMockGoal(UUID.randomUUID());
            goals.add(goal);
        }
        return goals;
    }

    private Goal createMockGoal(UUID id) {
        Goal goal = mock(Goal.class);
        when(goal.getId()).thenReturn(id);
        when(goal.getTitle()).thenReturn("테스트 목표");
        return goal;
    }

    private List<SubGoal> createMockSubGoals() {
        List<SubGoal> subGoals = new ArrayList<>();

        SubGoal subGoal3 = mock(SubGoal.class);
        when(subGoal3.getId()).thenReturn(UUID.randomUUID());
        when(subGoal3.getTitle()).thenReturn("세부목표 3");
        when(subGoal3.getOrder()).thenReturn(3);

        SubGoal subGoal1 = mock(SubGoal.class);
        when(subGoal1.getId()).thenReturn(UUID.randomUUID());
        when(subGoal1.getTitle()).thenReturn("세부목표 1");
        when(subGoal1.getOrder()).thenReturn(1);

        SubGoal subGoal2 = mock(SubGoal.class);
        when(subGoal2.getId()).thenReturn(UUID.randomUUID());
        when(subGoal2.getTitle()).thenReturn("세부목표 2");
        when(subGoal2.getOrder()).thenReturn(2);

        subGoals.add(subGoal3);
        subGoals.add(subGoal1);
        subGoals.add(subGoal2);

        return subGoals;
    }

    private List<TodoItemDto> createMockTodoItems(int count) {
        List<TodoItemDto> todos = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            TodoItemDto todo = new TodoItemDto(
                    UUID.randomUUID(),
                    "테스트 투두 " + (i + 1),
                    LocalDate.now(),
                    TodoStatus.INCOMPLETE,
                    LocalDateTime.now(),
                    new TodoResultItemDto(UUID.randomUUID(), Emotion.PROUD, "todo result", ""));
            todos.add(todo);
        }
        return todos;
    }
}