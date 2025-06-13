package kr.co.api.todo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kr.co.api.todo.rqrs.TodoResultRs;
import kr.co.domain.todo.Emotion;
import kr.co.domain.todo.TodoResult;
import kr.co.domain.todo.TodoStatus;
import kr.co.domain.todo.dto.TodoSummary;
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
    class GetTodosBySubGoalTest {

        @Test
        void 세부목표_ID로_투두_리스트_조회() {
            // given
            List<TodoSummary> mockSummaries = createMockTodoSummaries(3);

            when(todoRepository.findIncompleteOrTodayTodosBySubGoalId(subGoalId))
                    .thenReturn(mockSummaries);

            // when
            List<TodoSummary> todos = todoQueryService.getIncompleteOrTodayTodosBySubGoalId(
                    subGoalId);

            // then
            assertThat(todos).isNotNull();
            assertThat(todos).hasSize(3);
            verify(todoRepository).findIncompleteOrTodayTodosBySubGoalId(subGoalId);
        }

    }

    @Nested
    @DisplayName("유저의 투두 리스트 조회 테스트")
    class GetTodosByUserTest {

        @Test
        void 유저_ID로_투두_리스트_조회() {
            // given
            int page = 0;
            int size = 10;
            List<TodoSummary> mockSummaries = createMockTodoSummaries(3);

            when(todoRepository.findAllByUserId(userId)).thenReturn(mockSummaries);

            // when
            List<TodoSummary> todos = todoQueryService.getTodosByUserId(userId);

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
            List<TodoSummary> emptyList = Collections.emptyList();
            when(todoRepository.findAllByUserId(nonExistsUserId)).thenReturn(emptyList);

            // when
            List<TodoSummary> todos = todoQueryService.getTodosByUserId(nonExistsUserId);

            // then
            assertThat(todos).isEmpty();
            verify(todoRepository).findAllByUserId(nonExistsUserId);
        }
    }

    @Nested
    @DisplayName("투두 ID로 투두 결과 조회 테스트")
    class GetTodoResultByTodoIdTest {

        @Test
        void 투두_ID로_결과_조회() {
            // given
            TodoResult mockResult = TodoResult.createTodoResult()
                    .id(UUID.randomUUID())
                    .todoId(todoId)
                    .emotion(Emotion.PROUD)
                    .content("투두 완료!")
                    .filePath("todo/file.jpg")
                    .build();

            when(todoResultRepository.findByTodoId(todoId)).thenReturn(Optional.of(mockResult));
            when(todoRepository.existsById(todoId)).thenReturn(true);

            // when
            Optional<TodoResultRs> result = todoQueryService.getTodoResultByTodoId(todoId);

            // then
            assertThat(result).isNotNull();
            assertThat(result).isPresent();
            assertThat(result.get().todoId()).isEqualTo(todoId);
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
            Optional<TodoResultRs> result = todoQueryService.getTodoResultByTodoId(todoId);

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

    private List<TodoSummary> createMockTodoSummaries(int count) {
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
}