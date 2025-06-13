package kr.co.infra.rdb.todo.util;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.TodoStatus;
import kr.co.infra.rdb.todo.entity.TodoEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TodoMapper 테스트")
public class TodoMapperTest {

    @Test
    void 투두_엔티티에서_도메인_모델로_변환() {
        // given
        UUID id = UUID.randomUUID();
        UUID subGoalId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        LocalDate date = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        TodoEntity entity = new TodoEntity(
                id,
                subGoalId,
                authorId,
                "투두 제목",
                date,
                TodoStatus.INCOMPLETE,
                now,
                now
        );

        // when
        Todo todo = TodoMapper.toDomain(entity);

        //then
        assertThat(todo.getId()).isEqualTo(id);
        assertThat(todo.getSubGoalId()).isEqualTo(subGoalId);
        assertThat(todo.getAuthorId()).isEqualTo(authorId);
        assertThat(todo.getTitle()).isEqualTo("투두 제목");
        assertThat(todo.getDate()).isEqualTo(date);
        assertThat(todo.getStatus()).isEqualTo(TodoStatus.INCOMPLETE);
        assertThat(todo.getCreatedAt()).isEqualTo(now);
        assertThat(todo.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void 투두_모델에서_엔티티로_변환() {
        // given
        UUID id = UUID.randomUUID();
        UUID subGoalId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        LocalDate date = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        Todo domain = Todo.createTodo()
                .id(id)
                .subGoalId(subGoalId)
                .authorId(authorId)
                .title("투두 제목")
                .date(date)
                .status(TodoStatus.INCOMPLETE)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // when
        TodoEntity entity = TodoMapper.toEntity(domain);

        // then
        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getSubGoalId()).isEqualTo(subGoalId);
        assertThat(entity.getAuthorId()).isEqualTo(authorId);
        assertThat(entity.getTitle()).isEqualTo("투두 제목");
        assertThat(entity.getDate()).isEqualTo(date);
        assertThat(entity.getStatus()).isEqualTo(TodoStatus.INCOMPLETE);
        assertThat(entity.getCreatedAt()).isEqualTo(now);
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
    }
}