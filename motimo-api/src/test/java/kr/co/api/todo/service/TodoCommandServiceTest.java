package kr.co.api.todo.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.UUID;
import kr.co.domain.common.exception.AccessDeniedException;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.exception.TodoNotFoundException;
import kr.co.domain.todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TodoCommandServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoCommandService todoCommandService;

    private final UUID userId = UUID.randomUUID();
    private final UUID subGoalId = UUID.randomUUID();
    private final UUID todoId = UUID.randomUUID();

    @Test
    void 투두_생성_성공() {
        // given
        // when
        todoCommandService.createTodo(userId, subGoalId, "title", LocalDate.now());
        // then
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    void 투두결과_제출_취소_성공() {
        // given
        Todo todo = mock(Todo.class);
        given(todoRepository.findById(todoId)).willReturn(todo);
        willDoNothing().given(todo).validateAuthor(userId);

        // when
        todoCommandService.cancelTodoResult(userId, todoId);

        // then
        verify(todo).cancelCompletion();
        verify(todoRepository).save(todo);
    }

    @Test
    void 없는_투두_아이디로_투두결과_제출시_예외반환() {
        // given
        when(todoRepository.findById(todoId)).thenThrow(new TodoNotFoundException());

        // when & then
        assertThatThrownBy(() -> todoCommandService.cancelTodoResult(userId, todoId))
                .isInstanceOf(TodoNotFoundException.class);
    }

    @Test
    void 작성자가_아닌_유저가_투두결과_제출_취소_요청시_예외반환() {
        // given
        Todo todo = mock(Todo.class);
        given(todoRepository.findById(todoId)).willReturn(todo);
        willThrow(AccessDeniedException.class).given(todo).validateAuthor(userId);

        // when
        assertThatThrownBy(() -> todoCommandService.cancelTodoResult(userId, todoId))
                .isInstanceOf(AccessDeniedException.class);

        // then
        verify(todo, never()).cancelCompletion();
        verify(todoRepository, never()).save(any());
    }

    @Test
    void 투두_삭제_성공() {
        // given
        Todo todo = mock(Todo.class);
        given(todoRepository.findById(todoId)).willReturn(todo);
        willDoNothing().given(todo).validateAuthor(userId);

        // when
        todoCommandService.deleteById(userId, todoId);

        // then
        verify(todoRepository).deleteById(todoId);
    }

    @Test
    void 없는_투두를_삭제할_경우_예외반환() {
        // given
        when(todoRepository.findById(todoId)).thenThrow(new TodoNotFoundException());

        // when & then
        assertThatThrownBy(() -> todoCommandService.deleteById(userId, todoId))
                .isInstanceOf(TodoNotFoundException.class);
    }

    @Test
    void 작성자가_아닌_유저가_투두결과_삭제_요청시_예외반환() {
        // given
        Todo todo = mock(Todo.class);
        given(todoRepository.findById(todoId)).willReturn(todo);
        willThrow(AccessDeniedException.class).given(todo).validateAuthor(userId);

        // when
        assertThatThrownBy(() -> todoCommandService.deleteById(userId, todoId))
                .isInstanceOf(AccessDeniedException.class);

        // then
        verify(todoRepository, never()).deleteById(todoId);
    }
}
