package kr.co.api.todo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.exception.TodoNotFoundException;
import kr.co.domain.todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TodoQueryServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoQueryService todoQueryService;

    private final UUID todoId = UUID.randomUUID();
    private final UUID subGoalId = UUID.randomUUID();
    private final UUID userId = UUID.randomUUID();
    private final Todo todo = Todo.builder()
            .id(todoId)
            .authorId(userId)
            .subGoalId(subGoalId)
            .title("title")
            .date(LocalDate.now())
            .build();

    @Test
    void 투두_아이디로_조회_성공() {
        // given
        given(todoRepository.findById(todoId)).willReturn(todo);

        // when
        Todo result = todoQueryService.getTodo(todoId);

        // then
        assertThat(result).isEqualTo(todo);
    }

    @Test
    void 없는_아이디로_투두를_조회시_예외반환() {
        // given
        when(todoRepository.findById(todoId)).thenThrow(new TodoNotFoundException());

        // when & then
        assertThatThrownBy(() -> todoQueryService.getTodo(todoId))
                .isInstanceOf(TodoNotFoundException.class);
    }

    @Test
    void 세부목표_아이디를_통해_투두_리스트_조회() {
        // given
        CustomSlice<Todo> todoList = new CustomSlice<>(List.of(todo), false);
        given(todoRepository.findAllBySubGoalId(subGoalId, 0, 10)).willReturn(todoList);

        // when
        CustomSlice<Todo> result = todoQueryService.getTodosBySubGoal(subGoalId, 0, 10);

        // then
        assertThat(result.content()).contains(todo);
    }

    @Test
    void 유저_아이디를_통해_투두_리스트_조회() {
        // given
        CustomSlice<Todo> todoList = new CustomSlice<>(List.of(todo), false);
        given(todoRepository.findAllByUserId(userId, 0, 10)).willReturn(todoList);

        // when
        CustomSlice<Todo> result = todoQueryService.getTodosByUser(userId, 0, 10);

        // then
        assertThat(result.content()).contains(todo);
    }

}
