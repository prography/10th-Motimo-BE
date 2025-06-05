package kr.co.api.goal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;
import kr.co.api.goal.service.GoalCommandService;
import kr.co.domain.goal.DueDate;
import kr.co.domain.goal.Goal;
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

    @Test
    void 목표_정상_생성() {
        // given
        final UUID uuid = UUID.randomUUID();
        final String goalTitle = "goal title";

        final List<SubGoal> subGoals = List.of(
                SubGoal.createSubGoal().title("sub goal title").importance(1).build());

        final DueDate dueDate = DueDate.of(2);

        Goal testGoal = Goal.createGoal().userId(uuid).title(goalTitle).dueDate(dueDate)
                .subGoals(subGoals).build();

        when(goalRepository.save(any(Goal.class))).thenReturn(testGoal);

        // when
        goalCommandService.createGoal(uuid, goalTitle, dueDate, subGoals);

        // then
        verify(goalRepository).save(goalCaptor.capture());
        Goal savedGoal = goalCaptor.getValue();

        assertThat(savedGoal).isNotNull();
        assertThat(savedGoal.getTitle()).isEqualTo(goalTitle);
        assertThat(savedGoal.getSubGoals()).hasSize(1);
        verify(goalRepository, times(1)).save(any(Goal.class));

    }
}