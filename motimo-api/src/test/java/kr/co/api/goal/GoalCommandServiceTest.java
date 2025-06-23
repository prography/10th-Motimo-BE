package kr.co.api.goal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import kr.co.api.goal.dto.GoalCreateDto;
import kr.co.api.goal.dto.SubGoalCreateDto;
import kr.co.api.goal.service.GoalCommandService;
import kr.co.domain.goal.DueDate;
import kr.co.domain.goal.Goal;
import kr.co.domain.goal.exception.GoalCompleteFailedException;
import kr.co.domain.goal.exception.GoalErrorCode;
import kr.co.domain.goal.repository.GoalRepository;
import kr.co.domain.subGoal.SubGoal;
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
                new SubGoalCreateDto("sub goal title", 3)
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

    private Goal createTestGoal(UUID userId) {
        final List<SubGoal> subGoals = List.of(
                SubGoal.createSubGoal().title("sub goal title").importance(1).build());
        final DueDate dueDate = DueDate.of(2);
        return  Goal.createGoal().userId(userId).title(goalTitle).dueDate(dueDate)
                .subGoals(subGoals).build();
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
        goalCommandService.goalComplete(userId, goalId);

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
                SubGoal.createSubGoal().title("sub goal title").importance(1).build());

        Goal testGoal = dommyTestGoal(userId, goalId, subGoals);
        when(goalRepository.findById(goalId)).thenReturn(testGoal);

        // when & than
        assertThatThrownBy(() -> goalCommandService.goalComplete(userId, goalId))
                .isInstanceOf(GoalCompleteFailedException.class)
                .hasMessage(GoalErrorCode.GOAL_COMPLETION_CONDITION_NOT_MATCHED.getMessage());
    }

    private Goal dommyTestGoal(UUID userId, UUID goalId, List<SubGoal> subGoals) {
        final DueDate dueDate = DueDate.of(2);
        return  Goal.builder().id(goalId).userId(userId).title(goalTitle).dueDate(dueDate)
                .createdAt(LocalDateTime.of(2025,6,19,4,40))
                .updatedAt(LocalDateTime.now())
                .subGoals(subGoals).build();
    }

}