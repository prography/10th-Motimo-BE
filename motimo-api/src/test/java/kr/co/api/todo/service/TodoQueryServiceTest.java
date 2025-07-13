package kr.co.api.todo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kr.co.domain.goal.dto.GoalTodoCount;
import kr.co.domain.todo.Emotion;
import kr.co.domain.todo.TodoResult;
import kr.co.domain.todo.TodoStatus;
import kr.co.domain.todo.dto.TodoItemDto;
import kr.co.domain.todo.dto.TodoResultItemDto;
import kr.co.domain.todo.exception.TodoErrorCode;
import kr.co.domain.todo.exception.TodoNotFoundException;
import kr.co.domain.todo.repository.TodoRepository;
import kr.co.domain.todo.repository.TodoResultRepository;
import kr.co.infra.storage.service.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("TodoQueryService 테스트")
class TodoQueryServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private TodoResultRepository todoResultRepository;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private TodoQueryService todoQueryService;

    private UUID userId;
    private UUID subGoalId;
    private UUID todoId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        subGoalId = UUID.randomUUID();
        todoId = UUID.randomUUID();
    }

    @Nested
    @DisplayName("세부 목표 아이디로 미완료 상태이거나 오늘의 투두 리스트 조회 테스트")
    class GetInCompletedOrTodayTodosBySubGoalTest {

        @Test
        void 세부목표_ID로_미완료_또는_오늘의_투두_리스트_조회() {
            // given
            LocalDate today = LocalDate.now();
            List<TodoItemDto> mockTodoItems = createMockTodoItems(3);

            when(todoRepository.findIncompleteOrDateTodosBySubGoalId(subGoalId, today))
                    .thenReturn(mockTodoItems);
            when(storageService.getFileUrl(anyString())).thenReturn("https://example.com/file.jpg");

            // when
            List<TodoItemDto> todos = todoQueryService.getIncompleteOrTodayTodosBySubGoalId(
                    subGoalId);

            // then
            assertThat(todos).isNotNull();
            assertThat(todos).hasSize(3);
            verify(todoRepository).findIncompleteOrDateTodosBySubGoalId(subGoalId, today);
        }

        @Test
        void 투두가_우선순위에_따라_정렬되어_반환된다() {
            // given
            LocalDate today = LocalDate.now();
            List<TodoItemDto> mockTodos = Arrays.asList(
                    createTodoItem(TodoStatus.COMPLETE, today.minusDays(1), createTodoResultItem()),
                    // 완료 + 결과 있음
                    createTodoItem(TodoStatus.INCOMPLETE, today, null), // 미완료
                    createTodoItem(TodoStatus.COMPLETE, today.plusDays(1), null) // 완료 + 결과 없음
            );

            when(todoRepository.findIncompleteOrDateTodosBySubGoalId(subGoalId, today))
                    .thenReturn(mockTodos);

            // when
            List<TodoItemDto> todos = todoQueryService.getIncompleteOrTodayTodosBySubGoalId(
                    subGoalId);

            // then
            assertThat(todos).hasSize(3);
            // 미완료가 먼저 와야 함
            assertThat(todos.get(0).status()).isEqualTo(TodoStatus.INCOMPLETE);
            // 완료 + 결과 없음이 두 번째
            assertThat(todos.get(1).status()).isEqualTo(TodoStatus.COMPLETE);
            assertThat(todos.get(1).todoResultItem()).isNull();
            // 완료 + 결과 있음이 마지막
            assertThat(todos.get(2).status()).isEqualTo(TodoStatus.COMPLETE);
            assertThat(todos.get(2).todoResultItem()).isNotNull();
        }
    }

    @Nested
    @DisplayName("세부 목표 아이디로 모든 투두 리스트 조회 테스트")
    class GetTodosBySubGoalTest {

        @Test
        void 세부목표_ID로_모든_투두_리스트_조회() {
            // given
            List<TodoItemDto> mockTodoItems = createMockTodoItems(5);

            when(todoRepository.findAllBySubGoalId(subGoalId)).thenReturn(mockTodoItems);
            when(storageService.getFileUrl(anyString())).thenReturn("https://example.com/file.jpg");

            // when
            List<TodoItemDto> todos = todoQueryService.getTodosBySubGoalId(subGoalId);

            // then
            assertThat(todos).isNotNull();
            assertThat(todos).hasSize(5);
            verify(todoRepository).findAllBySubGoalId(subGoalId);
        }

        @Test
        void 투두가_존재하지_않는_세부목표_ID로_조회시_빈_리스트_반환() {
            // given
            UUID nonExistsSubGoalId = UUID.randomUUID();
            List<TodoItemDto> emptyList = Collections.emptyList();
            when(todoRepository.findAllBySubGoalId(nonExistsSubGoalId)).thenReturn(emptyList);

            // when
            List<TodoItemDto> todos = todoQueryService.getTodosBySubGoalId(nonExistsSubGoalId);

            // then
            assertThat(todos).isEmpty();
            verify(todoRepository).findAllBySubGoalId(nonExistsSubGoalId);
        }
    }

    @Nested
    @DisplayName("유저의 투두 리스트 조회 테스트")
    class GetTodosByUserTest {

        @Test
        void 유저_ID로_투두_리스트_조회() {
            // given
            List<TodoItemDto> mockTodoItems = createMockTodoItems(3);

            when(todoRepository.findAllByUserId(userId)).thenReturn(mockTodoItems);
            when(storageService.getFileUrl(anyString())).thenReturn("https://example.com/file.jpg");

            // when
            List<TodoItemDto> todos = todoQueryService.getTodosByUserId(userId);

            // then
            assertThat(todos).isNotNull();
            assertThat(todos).hasSize(3);
            verify(todoRepository).findAllByUserId(userId);
        }

        @Test
        void 투두가_존재하지_않는_유저_ID로_조회시_빈_리스트_반환() {
            // given
            int page = 0;
            int size = 10;
            UUID nonExistsUserId = UUID.randomUUID();
            List<TodoItemDto> emptyList = Collections.emptyList();
            when(todoRepository.findAllByUserId(nonExistsUserId)).thenReturn(emptyList);

            // when
            List<TodoItemDto> todos = todoQueryService.getTodosByUserId(nonExistsUserId);

            // then
            assertThat(todos).isEmpty();
            verify(todoRepository).findAllByUserId(nonExistsUserId);
        }

        @Test
        void 투두_결과의_파일_URL이_없는_투두_결과가_있는_경우() {
            // given
            TodoItemDto todoWithNullFileUrl = createTodoItem(TodoStatus.COMPLETE, LocalDate.now(),
                    new TodoResultItemDto(UUID.randomUUID(), Emotion.PROUD, "content", null));
            when(todoRepository.findAllByUserId(userId)).thenReturn(List.of(todoWithNullFileUrl));

            // when
            List<TodoItemDto> todos = todoQueryService.getTodosByUserId(userId);

            // then
            assertThat(todos).hasSize(1);
            assertThat(todos.getFirst().todoResultItem().fileUrl()).isNull();
            verify(storageService, never()).getFileUrl(anyString());
        }
    }

    @Nested
    @DisplayName("투두 ID로 투두 결과 조회 테스트")
    class GetTodoResultByTodoIdTest {

        @Test
        void 투두_ID로_결과_조회() {
            // given
            TodoResult mockResult = TodoResult.builder()
                    .id(UUID.randomUUID())
                    .todoId(todoId)
                    .emotion(Emotion.PROUD)
                    .content("투두 완료!")
                    .filePath("todo/file.jpg")
                    .build();

            when(todoResultRepository.findByTodoId(todoId)).thenReturn(Optional.of(mockResult));
            when(todoRepository.existsById(todoId)).thenReturn(true);

            // when
            Optional<TodoResultItemDto> result = todoQueryService.getTodoResultByTodoId(todoId);

            // then
            assertThat(result).isNotNull();
            assertThat(result).isPresent();
            assertThat(result.get().emotion()).isEqualTo(Emotion.PROUD);
            assertThat(result.get().content()).isEqualTo("투두 완료!");
            verify(todoResultRepository).findByTodoId(todoId);
        }

        @Test
        @DisplayName("Todo ID로 결과 조회 - 결과 없음")
        void 투두_ID로_결과_조회시_결과가_없는_경우() {
            // given
            when(todoResultRepository.findByTodoId(todoId)).thenReturn(Optional.empty());
            when(todoRepository.existsById(todoId)).thenReturn(true);

            // when
            Optional<TodoResultItemDto> result = todoQueryService.getTodoResultByTodoId(todoId);

            // then
            assertThat(result).isEmpty();
            verify(todoResultRepository).findByTodoId(todoId);
        }

        @Test
        void 존재하지_않는_투두_ID로_조회시_예외_발생() {
            // given
            UUID nonExistsTodoId = UUID.randomUUID();
            when(todoRepository.existsById(nonExistsTodoId)).thenReturn(false);
            // when & then
            assertThatThrownBy(() -> todoQueryService.getTodoResultByTodoId(nonExistsTodoId))
                    .isInstanceOf(TodoNotFoundException.class)
                    .hasMessage(TodoErrorCode.TODO_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("목표 ID들로 투두 개수 조회 테스트")
    class GetTodoCountsByGoalIdsTest {

        @Test
        void 목표_ID들로_투두_개수_조회() {
            // given
            List<UUID> goalIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID(),
                    UUID.randomUUID());
            List<GoalTodoCount> mockCounts = Arrays.asList(
                    new GoalTodoCount(goalIds.get(0), 5L, 4L),
                    new GoalTodoCount(goalIds.get(1), 3L, 3L),
                    new GoalTodoCount(goalIds.get(2), 0L, 0L)
            );

            when(todoRepository.countTodosByGoalIds(goalIds)).thenReturn(mockCounts);

            // when
            List<GoalTodoCount> counts = todoQueryService.getTodoCountsByGoalIds(goalIds);

            // then
            assertThat(counts).hasSize(3);
            assertThat(counts.get(0).goalId()).isEqualTo(goalIds.get(0));
            assertThat(counts.get(0).todoCount()).isEqualTo(5L);
            assertThat(counts.get(1).goalId()).isEqualTo(goalIds.get(1));
            assertThat(counts.get(1).todoCount()).isEqualTo(3L);
            assertThat(counts.get(2).goalId()).isEqualTo(goalIds.get(2));
            assertThat(counts.get(2).todoCount()).isEqualTo(0L);
            verify(todoRepository).countTodosByGoalIds(goalIds);
        }

        @Test
        void 빈_목표_ID_리스트로_조회시_빈_리스트_반환() {
            // given
            List<UUID> emptyGoalIds = Collections.emptyList();
            List<GoalTodoCount> emptyCounts = Collections.emptyList();

            when(todoRepository.countTodosByGoalIds(emptyGoalIds)).thenReturn(emptyCounts);

            // when
            List<GoalTodoCount> counts = todoQueryService.getTodoCountsByGoalIds(emptyGoalIds);

            // then
            assertThat(counts).isEmpty();
            verify(todoRepository).countTodosByGoalIds(emptyGoalIds);
        }
    }

    private List<TodoItemDto> createMockTodoItems(int count) {
        List<TodoItemDto> items = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            items.add(new TodoItemDto(
                    UUID.randomUUID(),
                    "테스트 투두",
                    LocalDate.now(),
                    TodoStatus.INCOMPLETE,
                    LocalDateTime.now(),
                    new TodoResultItemDto(UUID.randomUUID(), Emotion.PROUD, "todo result", "")));
        }
        return items;
    }

    private TodoItemDto createTodoItem(TodoStatus status, LocalDate date,
            TodoResultItemDto resultItem) {
        return new TodoItemDto(
                UUID.randomUUID(),
                "테스트 투두",
                date,
                status,
                LocalDateTime.now(),
                resultItem
        );
    }

    private TodoResultItemDto createTodoResultItem() {
        return new TodoResultItemDto(
                UUID.randomUUID(),
                Emotion.PROUD,
                "todo result",
                "file.jpg"
        );
    }
}