package kr.co.api.subgoal.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
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
@DisplayName("Sub Goal Service Command 테스트")
class SubGoalCommandServiceTest {

    @Mock
    private SubGoalRepository subGoalRepository;

    @InjectMocks
    private SubGoalCommandService subGoalCommandService;

    @Captor
    private ArgumentCaptor<SubGoal> subGoalCaptor;

    @Test
    void 세부_목표_완료_토글_정상_동작() {
        // given
        final UUID userId = UUID.randomUUID();
        final UUID subGoalId = UUID.randomUUID();
        final LocalDateTime requestTime = LocalDateTime.now();

        Random random = new Random();
        final boolean nowIsCompleted = random.nextBoolean();

        SubGoal testSubGoal = testSubGoal(userId, subGoalId, nowIsCompleted);

        when(subGoalRepository.findById(subGoalId)).thenReturn(testSubGoal);
        when(subGoalRepository.update(any(SubGoal.class))).thenReturn(testSubGoal);

        // when
        subGoalCommandService.toggleSubGoalComplete(userId, subGoalId);

        // then
        verify(subGoalRepository).update(subGoalCaptor.capture());
        SubGoal updatedGoal = subGoalCaptor.getValue();


        assertThat(updatedGoal.getId()).isEqualTo(testSubGoal.getId());
        assertThat(updatedGoal.isCompleted()).isNotEqualTo(nowIsCompleted);
        assertThat(updatedGoal.getCompletedChangedAt()).isAfter(requestTime);
    }

    private SubGoal testSubGoal(UUID userId, UUID subGoalId, boolean isCompleted) {
        return SubGoal.builder()
                .id(subGoalId)
                .userId(userId)
                .goalId(UUID.randomUUID())
                .title("title")
                .completed(isCompleted)
                .order(1)
                .build();

    }
}