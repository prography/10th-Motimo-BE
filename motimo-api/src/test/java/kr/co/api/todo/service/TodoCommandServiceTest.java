package kr.co.api.todo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.UUID;
import kr.co.domain.common.event.Events;
import kr.co.domain.common.event.FileDeletedEvent;
import kr.co.domain.common.exception.AccessDeniedException;
import kr.co.domain.todo.Emotion;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.TodoResult;
import kr.co.domain.todo.TodoStatus;
import kr.co.domain.todo.exception.TodoErrorCode;
import kr.co.domain.todo.exception.TodoNotFoundException;
import kr.co.domain.todo.repository.TodoRepository;
import kr.co.domain.todo.repository.TodoResultRepository;
import kr.co.infra.storage.exception.StorageErrorCode;
import kr.co.infra.storage.exception.StorageException;
import kr.co.infra.storage.service.StorageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class TodoCommandServiceTest {

    private static final Logger log = LoggerFactory.getLogger(TodoCommandServiceTest.class);
    @Mock
    private TodoRepository todoRepository;

    @Mock
    private TodoResultRepository todoResultRepository;

    @Mock
    private StorageService storageService;

    private MockedStatic<Events> mockedEvents;

    @InjectMocks
    private TodoCommandService todoCommandService;

    private UUID userId = UUID.randomUUID();
    private UUID subGoalId = UUID.randomUUID();
    private UUID todoId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        subGoalId = UUID.randomUUID();
        todoId = UUID.randomUUID();
        mockedEvents = mockStatic(Events.class);
    }

    @AfterEach
    void close() {
        mockedEvents.close();
    }

    @Nested
    @DisplayName("투두 생성 테스트")
    class CreateTodoTest {

        @Test
        void 투두_생성_성공() {
            String title = "새로운 할일";
            LocalDate date = LocalDate.now();

            // when
            todoCommandService.createTodo(userId, subGoalId, title, date);

            // then
            ArgumentCaptor<Todo> todoCaptor = ArgumentCaptor.forClass(Todo.class);
            verify(todoRepository).save(todoCaptor.capture());

            Todo savedTodo = todoCaptor.getValue();
            assertThat(savedTodo.getAuthorId()).isEqualTo(userId);
            assertThat(savedTodo.getSubGoalId()).isEqualTo(subGoalId);
            assertThat(savedTodo.getTitle()).isEqualTo(title);
            assertThat(savedTodo.getDate()).isEqualTo(date);
            assertThat(savedTodo.getStatus()).isEqualTo(TodoStatus.INCOMPLETE);
        }

        @Test
        void 투두_생성시_repository_저장_실패_시_예외_발생() {
            // given
            String title = "새로운 할일";
            LocalDate date = LocalDate.now();
            when(todoRepository.save(any(Todo.class)))
                    .thenThrow(new RuntimeException("Database error"));

            // when & then
            assertThatThrownBy(
                    () -> todoCommandService.createTodo(userId, subGoalId, title, date))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Database error");
        }

    }

    @Nested
    @DisplayName("투두 결과(기록) 제출 테스트")
    class SubmitTodoResultTest {

        Todo todo;

        @BeforeEach
        void setUp() {
            todo = Todo.createTodo()
                    .id(todoId)
                    .authorId(userId)
                    .subGoalId(subGoalId)
                    .title("Test Todo")
                    .date(LocalDate.now())
                    .status(TodoStatus.INCOMPLETE)
                    .build();
        }

        @Test
        void 파일_없이_투두_결과_제출_성공() {
            // given
            when(todoRepository.findById(todoId)).thenReturn(todo);
            Emotion emotion = Emotion.PROUD;
            String content = "투두 완료!";
            // when
            todoCommandService.submitTodoResult(userId, todoId, emotion, content, null);

            // then
            ArgumentCaptor<TodoResult> resultCaptor = ArgumentCaptor.forClass(TodoResult.class);
            verify(todoResultRepository).save(resultCaptor.capture());

            TodoResult savedResult = resultCaptor.getValue();
            assertThat(savedResult.getTodoId()).isEqualTo(todoId);
            assertThat(savedResult.getEmotion()).isEqualTo(emotion);
            assertThat(savedResult.getContent()).isEqualTo(content);
            assertThat(savedResult.getFilePath()).isEqualTo("");

            verify(storageService, never()).store(any(), any());
            mockedEvents.verify(() -> Events.publishEvent(any()), never());
        }

        @Test
        void 빈_파일로_할일_결과_제출() {
            // given
            MultipartFile file = mock(MultipartFile.class);
            when(todoRepository.findById(todoId)).thenReturn(todo);
            when(file.isEmpty()).thenReturn(true);
            Emotion emotion = Emotion.PROUD;
            String content = "투두 완료!";

            // when
            todoCommandService.submitTodoResult(userId, todoId, emotion, content, file);

            // then
            ArgumentCaptor<TodoResult> resultCaptor = ArgumentCaptor.forClass(TodoResult.class);
            verify(todoResultRepository).save(resultCaptor.capture());

            TodoResult savedResult = resultCaptor.getValue();
            assertThat(savedResult.getFilePath()).isEqualTo("");

            verify(storageService, never()).store(any(), any());
            mockedEvents.verify(() -> Events.publishEvent(any()), never());
        }

        @Test
        void 파일을_포함한_투두_결과_제출_성공() {
            // given
            String filename = "image.jpg";
            MultipartFile file = new MockMultipartFile("file", filename, "image/jpeg",
                    "file".getBytes());

            UUID fixedUuid = UUID.fromString("8ec9c39f-ae45-4c1a-b38f-0af834a88a4c");
            String filePath = String.format("todo/%s/%s", todoId, fixedUuid);
            Emotion emotion = Emotion.PROUD;
            String content = "투두 완료!";

            when(todoRepository.findById(todoId)).thenReturn(todo);
            doNothing().when(storageService).store(any(MultipartFile.class), anyString());

            try (MockedStatic<UUID> mockedUUID = mockStatic(UUID.class)) {
                mockedUUID.when(UUID::randomUUID).thenReturn(fixedUuid);

                // when
                todoCommandService.submitTodoResult(userId, todoId, emotion, content, file);

                // then
                ArgumentCaptor<TodoResult> resultCaptor = ArgumentCaptor.forClass(TodoResult.class);
                verify(todoResultRepository).save(resultCaptor.capture());

                TodoResult savedResult = resultCaptor.getValue();
                assertThat(savedResult.getFilePath()).isEqualTo(filePath);
                mockedEvents.verify(() -> Events.publishEvent(any(FileDeletedEvent.class)));
            }
        }

        @Test
        void 존재하지않는_투두ID_제출시_예외_발생() {
            // given
            UUID nonExistsTodoId = UUID.randomUUID();
            when(todoRepository.findById(nonExistsTodoId)).thenReturn(null);

            // when & then
            assertThatThrownBy(
                    () -> todoCommandService.submitTodoResult(userId, nonExistsTodoId,
                            Emotion.PROUD, "투두 완료!", null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void 작성자가_아닌_경우_예외_발생() {
            // given
            UUID otherUserId = UUID.randomUUID();
            Todo mockTodo = mock(Todo.class);
            when(todoRepository.findById(todoId)).thenReturn(mockTodo);
            doThrow(new AccessDeniedException(TodoErrorCode.TODO_ACCESS_DENIED))
                    .when(mockTodo).validateAuthor(otherUserId);

            // when & then
            assertThatThrownBy(() -> todoCommandService.submitTodoResult(otherUserId, todoId,
                    Emotion.PROUD, "투두 완료!", null))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage(TodoErrorCode.TODO_ACCESS_DENIED.getMessage());
        }

        @Test
        void 파일_저장_실패_시_예외_발생() {
            // given
            MultipartFile file = new MockMultipartFile("file", "filename.jpg", "image/jpeg",
                    "file".getBytes());
            when(todoRepository.findById(todoId)).thenReturn(todo);
            doThrow(new StorageException(StorageErrorCode.FILE_UPLOAD_FAILED))
                    .when(storageService).store(eq(file), anyString());

            // when & then
            assertThatThrownBy(() -> todoCommandService.submitTodoResult(userId, todoId,
                    Emotion.PROUD, "투두 완료!", file))
                    .isInstanceOf(StorageException.class)
                    .hasMessage(StorageErrorCode.FILE_UPLOAD_FAILED.getMessage());
        }
    }

    @Nested
    @DisplayName("todo 완료 처리 메서드 테스트")
    class ToggleTodoCompletionTest {

        @Test
        @DisplayName("투두 완료 상태 토글 성공")
        void 투두_완료_상태로_토글_성공() {
            // given
            Todo todo = Todo.createTodo()
                    .id(todoId)
                    .authorId(userId)
                    .subGoalId(subGoalId)
                    .title("Test Todo")
                    .date(LocalDate.now())
                    .status(TodoStatus.INCOMPLETE)
                    .build();
            when(todoRepository.findById(todoId)).thenReturn(todo);

            // when
            todoCommandService.toggleTodoCompletion(userId, todoId);

            // then
            assertThat(todo.getStatus()).isEqualTo(TodoStatus.COMPLETE);
            verify(todoRepository).save(todo);
        }

        @Test
        @DisplayName("존재하지 않는 할일 ID로 토글 시 예외 발생")
        void 존재하지_않는_할일_ID로_토글_시_예외_발생() {
            // given
            UUID nonExistentTodoId = UUID.randomUUID();
            when(todoRepository.findById(nonExistentTodoId)).thenReturn(null);

            // when & then
            assertThatThrownBy(
                    () -> todoCommandService.toggleTodoCompletion(userId, nonExistentTodoId))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void 작성자_아닌_경우_예외_발생() {
            // given
            Todo todo = mock(Todo.class);
            UUID otherUserId = UUID.randomUUID();
            when(todoRepository.findById(todoId)).thenReturn(todo);
            doThrow(new AccessDeniedException(TodoErrorCode.TODO_ACCESS_DENIED)).when(todo)
                    .validateAuthor(otherUserId);

            // when & then
            assertThatThrownBy(
                    () -> todoCommandService.toggleTodoCompletion(otherUserId, todoId))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage(TodoErrorCode.TODO_ACCESS_DENIED.getMessage());
        }
    }

    @Nested
    @DisplayName("투두 업데이트 테스트")
    class TodoUpdateTest {

        Todo todo;
        String newTitle;
        LocalDate newDate;

        @BeforeEach
        void setUp() {
            todo = Todo
                    .builder()
                    .authorId(userId)
                    .title("투두!")
                    .date(LocalDate.of(2025, 6, 1))
                    .build();
            newTitle = "새로운 투두!";
            newDate = LocalDate.of(2025, 6, 14);
        }

        @Test
        void 투두_업데이트_성공() {
            // given
            when(todoRepository.findById(todoId)).thenReturn(todo);

            // when
            todoCommandService.updateTodo(userId, todoId, newTitle, newDate);

            // then
            assertThat(todo.getTitle()).isEqualTo(newTitle);
            assertThat(todo.getDate()).isEqualTo(newDate);
            verify(todoRepository).save(todo);
        }

        @Test
        void 없는_투두에_대한_업데이트_요청시_예외반환() {
            // given
            when(todoRepository.findById(todoId)).thenThrow(new TodoNotFoundException());

            // when & then
            assertThatThrownBy(
                    () -> todoCommandService.updateTodo(userId, todoId, newTitle, newDate))
                    .isInstanceOf(TodoNotFoundException.class)
                    .hasMessage(TodoErrorCode.TODO_NOT_FOUND.getMessage());
        }

        @Test
        void 투두_대한_권한이_없는_유저인경우_예외반환() {
            // given
            UUID otherUserId = UUID.randomUUID();
            when(todoRepository.findById(todoId)).thenReturn(todo);

            // when & then
            assertThatThrownBy(
                    () -> todoCommandService.updateTodo(otherUserId, todoId, newTitle, newDate))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage(TodoErrorCode.TODO_ACCESS_DENIED.getMessage());
            verify(todoRepository).findById(todoId);
        }
    }

    @Nested
    @DisplayName("투두 아이디로 투두 삭제 테스트")
    class DeleteByIdTest {

        @Test
        void 투두_삭제_성공() {
            // given
            Todo todo = mock(Todo.class);
            when(todoRepository.findById(todoId)).thenReturn(todo);

            // when
            todoCommandService.deleteById(userId, todoId);

            // then
            verify(todo).validateAuthor(userId);
            verify(todoRepository).deleteById(todoId);
        }

        @Test
        void 존재하지_않는_투두_ID로_삭제_시_예외_발생() {
            // given
            UUID nonExistsTodoId = UUID.randomUUID();
            when(todoRepository.findById(nonExistsTodoId)).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> todoCommandService.deleteById(userId, nonExistsTodoId))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void 작성자가_아닌_경우_투두_삭제_시_예외_발생() {
            // given
            UUID otherUserId = UUID.randomUUID();
            Todo todo = mock(Todo.class);
            when(todoRepository.findById(todoId)).thenReturn(todo);
            doThrow(new AccessDeniedException(TodoErrorCode.TODO_ACCESS_DENIED)).when(todo)
                    .validateAuthor(otherUserId);

            // when & then
            assertThatThrownBy(() -> todoCommandService.deleteById(otherUserId, todoId))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage(TodoErrorCode.TODO_ACCESS_DENIED.getMessage());
        }

        @Test
        void repository_삭제_실패_시_예외_발생() {
            // given
            Todo todo = Todo.createTodo()
                    .id(todoId)
                    .authorId(userId)
                    .subGoalId(subGoalId)
                    .title("Test Todo")
                    .date(LocalDate.now())
                    .status(TodoStatus.INCOMPLETE)
                    .build();
            when(todoRepository.findById(todoId)).thenReturn(todo);
            doThrow(new RuntimeException("Database error")).when(todoRepository).deleteById(todoId);

            // when & then
            assertThatThrownBy(() -> todoCommandService.deleteById(userId, todoId))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Database error");
        }
    }
}