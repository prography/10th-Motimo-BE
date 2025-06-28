package kr.co.api.goal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import kr.co.api.goal.dto.GoalCreateDto;
import kr.co.api.goal.dto.GoalUpdateDto;
import kr.co.api.goal.dto.SubGoalCreateDto;
import kr.co.api.goal.dto.SubGoalUpdateDto;
import kr.co.api.goal.service.GoalCommandService;
import kr.co.domain.goal.DueDate;
import kr.co.domain.goal.Goal;
import kr.co.domain.goal.exception.GoalCompleteFailedException;
import kr.co.domain.goal.exception.GoalErrorCode;
import kr.co.domain.goal.repository.GoalRepository;
import kr.co.domain.subGoal.SubGoal;
import kr.co.domain.subGoal.repository.SubGoalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Goal Service Command 테스트")
class GoalCommandServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private SubGoalRepository subGoalRepository;

    @InjectMocks
    private GoalCommandService goalCommandService;

    @Captor
    private ArgumentCaptor<Goal> goalCaptor;

    final String goalTitle = "goal title";

    @Test
    void 목표_정상_생성() {
        // given
        final UUID uuid = UUID.randomUUID();

        List<SubGoalCreateDto> subGoalDtos = List.of(
                new SubGoalCreateDto("sub goal title")
        );

        GoalCreateDto dto = new GoalCreateDto(goalTitle, true, 3, null, subGoalDtos);

        Goal testGoal = createTestGoal(uuid);
        when(goalRepository.create(any(Goal.class))).thenReturn(testGoal);

        // when
        goalCommandService.createGoal(uuid, dto);

        // then
        verify(goalRepository).create(goalCaptor.capture());
        Goal savedGoal = goalCaptor.getValue();

        assertThat(savedGoal).isNotNull();
        assertThat(savedGoal.getTitle()).isEqualTo(goalTitle);
        assertThat(savedGoal.getSubGoals()).hasSize(1);
        verify(goalRepository, times(1)).create(any(Goal.class));

    }

    @Test
    void 목표_완료_처리_성공() {
        // given
        final UUID userId = UUID.randomUUID();
        final UUID goalId = UUID.randomUUID();
        final LocalDateTime requestTime = LocalDateTime.now();

        List<SubGoal> subGoals = new ArrayList<>();

        Goal testGoal = dommyTestGoal(userId, goalId, subGoals);
        when(goalRepository.findById(goalId)).thenReturn(testGoal);
        when(goalRepository.update(any(Goal.class))).thenReturn(testGoal);

        // when
        goalCommandService.completeGoal(userId, goalId);

        // than
        verify(goalRepository).update(goalCaptor.capture());
        Goal updatedGoal = goalCaptor.getValue();

        assertThat(updatedGoal.getId()).isEqualTo(testGoal.getId());
        assertThat(updatedGoal.getCreatedAt()).isEqualTo(testGoal.getCreatedAt());
        assertThat(updatedGoal.isCompleted()).isTrue();
        assertThat(updatedGoal.completedAt).isAfter(requestTime);
    }

    @Test
    void 세부목표_미완료시_목표_완료_불가() {
        // given
        final UUID userId = UUID.randomUUID();
        final UUID goalId = UUID.randomUUID();
        final List<SubGoal> subGoals = List.of(
                SubGoal.createSubGoal().title("sub goal title").order(1).build());

        Goal testGoal = dommyTestGoal(userId, goalId, subGoals);
        when(goalRepository.findById(goalId)).thenReturn(testGoal);

        // when & than
        assertThatThrownBy(() -> goalCommandService.completeGoal(userId, goalId))
                .isInstanceOf(GoalCompleteFailedException.class)
                .hasMessage(GoalErrorCode.GOAL_COMPLETION_CONDITION_NOT_MATCHED.getMessage());
    }

    @Test
    void 목표_정상_수정() {
        // given
        UUID userId = UUID.randomUUID();

        GoalUpdateDto dto = new GoalUpdateDto(
                "updated title",
                true,
                5,
                null,
                List.of(),
                Set.of()
        );

        Goal testGoal = createTestGoal(userId);
        when(goalRepository.findById(testGoal.getId())).thenReturn(testGoal);
        when(goalRepository.update(any(Goal.class))).thenReturn(testGoal);

        when(subGoalRepository.findAllByGoalId(testGoal.getId())).thenReturn(List.of());

        // when
        goalCommandService.updateGoal(userId, testGoal.getId(), dto);

        // then
        verify(goalRepository).update(goalCaptor.capture());
        Goal updatedGoal = goalCaptor.getValue();
        assertThat(updatedGoal.getTitle()).isEqualTo("updated title");
        assertThat(updatedGoal.getDueDate().getMonth()).isEqualTo(5);

        verify(subGoalRepository).findAllByGoalId(testGoal.getId());
        verify(subGoalRepository).upsertList(eq(testGoal.getId()), eq(List.of()));
        verify(subGoalRepository).deleteList(eq(Set.of()));
    }


    @Test
    void 목표_세부_목표_모두_정상_수정() {
        // given
        UUID userId = UUID.randomUUID();
        Goal testGoal = createTestGoal(userId);

        UUID existingSubGoalId = UUID.randomUUID();
        UUID deletedSubGoalId = UUID.randomUUID();
        SubGoal existingSubGoal = SubGoal.builder()
                .goalId(testGoal.getId())
                .title("original sub")
                .order(1)
                .id(existingSubGoalId)
                .build();

        List<SubGoalUpdateDto> updateDtos = List.of(
                new SubGoalUpdateDto(existingSubGoalId, "modified sub", 2), // 기존 수정
                new SubGoalUpdateDto(null, "new sub", 3)                    // 신규 추가
        );

        GoalUpdateDto dto = new GoalUpdateDto(
                "updated goal title",
                false,
                null,
                LocalDate.now().plusDays(7),
                updateDtos,
                Set.of(deletedSubGoalId)
        );

        when(goalRepository.findById(testGoal.getId())).thenReturn(testGoal);
        when(goalRepository.update(any(Goal.class))).thenReturn(testGoal);

        when(subGoalRepository.findAllByGoalId(testGoal.getId())).thenReturn(List.of(existingSubGoal));

        // when
        goalCommandService.updateGoal(userId, testGoal.getId(), dto);

        // then
        verify(goalRepository).update(goalCaptor.capture());
        Goal updatedGoal = goalCaptor.getValue();
        assertThat(updatedGoal.getTitle()).isEqualTo("updated goal title");

        verify(subGoalRepository).findAllByGoalId(testGoal.getId());

        // upsertList 검증
        ArgumentCaptor<List<SubGoal>> subGoalCaptor = ArgumentCaptor.forClass(List.class);
        verify(subGoalRepository).upsertList(eq(testGoal.getId()), subGoalCaptor.capture());
        List<SubGoal> savedSubGoals = subGoalCaptor.getValue();

        assertThat(savedSubGoals).hasSize(2);
        assertThat(savedSubGoals).anyMatch(sg -> sg.getTitle().equals("modified sub"));
        assertThat(savedSubGoals).anyMatch(sg -> sg.getTitle().equals("new sub"));

        // 삭제 리스트 검증
        verify(subGoalRepository).deleteList(eq(Set.of(deletedSubGoalId)));
    }


    private Goal dommyTestGoal(UUID userId, UUID goalId, List<SubGoal> subGoals) {
        final DueDate dueDate = DueDate.of(2);
        return Goal.builder().id(goalId).userId(userId).title(goalTitle).dueDate(dueDate)
                .createdAt(LocalDateTime.of(2025, 6, 19, 4, 40))
                .updatedAt(LocalDateTime.now())
                .subGoals(subGoals).build();
    }


    private Goal createTestGoal(UUID userId) {
        final List<SubGoal> subGoals = List.of(
                SubGoal.createSubGoal().title("sub goal title").order(1).build());
        final DueDate dueDate = DueDate.of(2);
        return Goal.createGoal().userId(userId).title(goalTitle).dueDate(dueDate)
                .subGoals(subGoals).build();
    }
}