package kr.co.domain.todo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.domain.common.exception.AccessDeniedException;
import kr.co.domain.todo.exception.TodoErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Todo 도메인 테스트")
public class TodoTest {

    private UUID userId;
    private Todo todo;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        todo = Todo.builder()
                .id(UUID.randomUUID())
                .subGoalId(UUID.randomUUID())
                .userId(userId)
                .title("오늘의 투두")
                .date(LocalDate.of(2025, 5, 30))
                .status(TodoStatus.INCOMPLETE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void update_호출시_투두_제목과_투두_날짜가_변경된다() {
        // given
        String updateTitle = "새로운 투두";
        LocalDate updateDate = LocalDate.of(2025, 5, 31);

        // when
        todo.update(updateTitle, updateDate);

        // then
        assertThat(todo.getTitle()).isEqualTo(updateTitle);
        assertThat(todo.getDate()).isEqualTo(updateDate);
    }

    @Test
    void toggleCompletion_호출시_completed값이_토글된다() {
        // given
        Todo todo = Todo.builder()
                .status(TodoStatus.INCOMPLETE)
                .userId(UUID.randomUUID())
                .subGoalId(UUID.randomUUID())
                .build();
        // when
        todo.toggleCompletion();

        // then
        assertThat(todo.getStatus()).isEqualTo(TodoStatus.COMPLETE);
        todo.toggleCompletion();
        assertThat(todo.getStatus()).isEqualTo(TodoStatus.INCOMPLETE);
    }

    @Test
    void 작성자가_아닌지_확인시_일치할_경우_정상_동작() {
        // given
        // when & then
        assertThatCode(() -> todo.validateOwner(userId)).doesNotThrowAnyException();
    }

    @Test
    void 작성자가_아닌지_확인시_불일치할_경우_예외_발생() {
        // given
        UUID otherUserId = UUID.randomUUID();
        // when & then
        assertThatThrownBy(() -> todo.validateOwner(otherUserId))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage(TodoErrorCode.TODO_ACCESS_DENIED.getMessage());
    }

}