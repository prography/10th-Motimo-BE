package kr.co.api.goal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
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
import kr.co.domain.common.event.Events;
import kr.co.domain.common.event.group.message.GoalTitleUpdatedEvent;
import kr.co.domain.goal.DueDate;
import kr.co.domain.goal.Goal;
import kr.co.domain.goal.exception.GoalCompleteFailedException;
import kr.co.domain.goal.exception.GoalErrorCode;
import kr.co.domain.goal.repository.GoalRepository;
import kr.co.domain.subGoal.SubGoal;
import kr.co.domain.subGoal.repository.SubGoalRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
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

    private MockedStatic<Events> mockedEvents;

    final String goalTitle = "goal title";

    @BeforeEach
    void setUp() {
        mockedEvents = mockStatic(Events.class);
    }

    @AfterEach
    void clear() {
        mockedEvents.close();
    }

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

        // when
        goalCommandService.updateGoal(userId, testGoal.getId(), dto);

        // then
        verify(goalRepository).update(goalCaptor.capture());
        Goal updatedGoal = goalCaptor.getValue();
        assertThat(updatedGoal.getTitle()).isEqualTo("updated title");
        assertThat(updatedGoal.getDueDate().getMonth()).isEqualTo(5);
    }


    @Test
    void 목표_세부목표_정상_수정() {
        // given
        UUID userId = UUID.randomUUID();
        UUID goalId = UUID.randomUUID();

        SubGoal existingSubGoal = SubGoal.builder()
                .id(UUID.randomUUID())
                .goalId(goalId)
                .title("old subgoal")
                .order(1)
                .build();

        GoalUpdateDto dto = createGoalUpdateDto(existingSubGoal);

        Goal testGoal = dommyTestGoal(userId, goalId,
                convertToSubGoals(dto.subGoals(), goalId, userId));
        testGoal.putSubGoals(List.of(existingSubGoal));

        when(goalRepository.findById(testGoal.getId())).thenReturn(testGoal);
        when(goalRepository.update(any(Goal.class))).thenReturn(testGoal);

        // when
        goalCommandService.updateGoal(userId, testGoal.getId(), dto);

        // then
        verify(goalRepository).update(goalCaptor.capture());
        Goal updatedGoal = goalCaptor.getValue();

        // 목표 수정 검증
        assertThat(updatedGoal.getTitle()).isEqualTo("updated goal title");
        assertThat(updatedGoal.getDueDate().getDate()).isEqualTo(LocalDate.of(2025, 12, 31));

        // 세부목표 수정 검증
        List<SubGoal> updatedSubGoals = updatedGoal.getSubGoals();

        SubGoal modifiedSubGoal = updatedSubGoals.stream()
                .filter(sg -> sg.getId().equals(existingSubGoal.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(modifiedSubGoal.getTitle()).isEqualTo("updated subgoal title");
        assertThat(modifiedSubGoal.getOrder()).isEqualTo(2);

        // 새로운 subgoal이 추가 검증
        SubGoal addedSubGoal = updatedSubGoals.stream()
                .filter(sg -> sg.getTitle().equals("new subgoal"))
                .findFirst()
                .orElseThrow();
        assertThat(addedSubGoal.getOrder()).isEqualTo(3);
    }

    @Test
    void 그룹에_참여한_목표_제목을_수정시_이벤트_발행() {
        // given
        UUID userId = UUID.randomUUID();
        UUID goalId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        String updatedTitle = "updated title";

        GoalUpdateDto dto = new GoalUpdateDto(
                updatedTitle,
                true,
                5,
                null,
                List.of(),
                Set.of()
        );

        Goal testGoal = dommyTestGoal(userId, goalId,
                convertToSubGoals(dto.subGoals(), goalId, userId));
        testGoal.joinGroup(groupId);
        when(goalRepository.findById(testGoal.getId())).thenReturn(testGoal);
        when(goalRepository.update(any(Goal.class))).thenReturn(testGoal);

        // when
        goalCommandService.updateGoal(userId, testGoal.getId(), dto);

        // then
        mockedEvents.verify(() -> Events.publishEvent(any(GoalTitleUpdatedEvent.class)));
    }

    @Test
    void 그룹에_참여한_목표의_제목_변경이_없으면_이벤트_미발행() {
        // given
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        UUID goalId = UUID.randomUUID();

        GoalUpdateDto dto = new GoalUpdateDto(
                goalTitle,
                true,
                5,
                null,
                List.of(),
                Set.of()
        );

        Goal testGoal = dommyTestGoal(userId, goalId,
                convertToSubGoals(dto.subGoals(), goalId, userId));
        testGoal.joinGroup(groupId);
        when(goalRepository.findById(testGoal.getId())).thenReturn(testGoal);
        when(goalRepository.update(any(Goal.class))).thenReturn(testGoal);

        // when
        goalCommandService.updateGoal(userId, testGoal.getId(), dto);

        // then
        mockedEvents.verify(() -> Events.publishEvent(any(GoalTitleUpdatedEvent.class)),
                never());
    }

    @Test
    void 그룹에_참여하지_않은_목표_제목을_수정시_이벤트_미발행() {
        // given
        UUID userId = UUID.randomUUID();
        String updatedTitle = "updated title";

        GoalUpdateDto dto = new GoalUpdateDto(
                updatedTitle,
                true,
                5,
                null,
                List.of(),
                Set.of()
        );

        Goal testGoal = createTestGoal(userId);
        when(goalRepository.findById(testGoal.getId())).thenReturn(testGoal);
        when(goalRepository.update(any(Goal.class))).thenReturn(testGoal);

        // when
        goalCommandService.updateGoal(userId, testGoal.getId(), dto);

        // then
        mockedEvents.verify(() -> Events.publishEvent(any(GoalTitleUpdatedEvent.class)),
                never());
    }

    private GoalUpdateDto createGoalUpdateDto(SubGoal existingSubGoal) {
        return new GoalUpdateDto(
                "updated goal title",
                false,
                null,
                LocalDate.of(2025, 12, 31),
                List.of(
                        new SubGoalUpdateDto(
                                existingSubGoal.getId(),
                                "updated subgoal title",
                                2
                        ),
                        new SubGoalUpdateDto(
                                null,
                                "new subgoal",
                                3
                        )
                ),
                Set.of()
        );
    }

    private List<SubGoal> convertToSubGoals(List<SubGoalUpdateDto> subGoalDtos, UUID goalId,
            UUID userId) {
        return subGoalDtos.stream()
                .map(dto -> SubGoal.builder()
                        .id(dto.updateId())
                        .goalId(goalId)
                        .userId(userId)
                        .title(dto.title())
                        .order(dto.order())
                        .build()
                )
                .toList();
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