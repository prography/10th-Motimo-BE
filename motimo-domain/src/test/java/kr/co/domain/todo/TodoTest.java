package kr.co.domain.todo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.domain.common.exception.AccessDeniedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TodoTest {

    private UUID authorId;
    private Todo todo;

    @BeforeEach
    void setUp() {
        authorId = UUID.randomUUID();
        todo = Todo.builder()
                .id(UUID.randomUUID())
                .subGoalId(UUID.randomUUID())
                .authorId(authorId)
                .title("오늘의 투두")
                .date(LocalDate.of(2025, 5, 30))
                .completed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void 투두_제목_및_date_수정() {
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
    void 투두_완료() {
        // given
        TodoResult result = new TodoResult(Emotion.HAPPY, "Great progress!", "http://image.url");

        // when
        todo.complete(result);

        // then
        assertThat(todo.isCompleted()).isTrue();
        assertThat(todo.getResult()).isEqualTo(result);
    }

    @Test
    void 투두_완료_취소() {
        // given
        // when
        todo.complete(new TodoResult(Emotion.SAD, "Sad result", "img"));
        todo.cancelCompletion();

        // then
        assertThat(todo.isCompleted()).isFalse();
    }

    @Test
    void 투두를_작성한_사람인지_확인_작성한_사람의_Id() {
        assertDoesNotThrow(() -> todo.validateAuthor(authorId));
    }

    @Test
    void 투두를_작성한_사람인지_확인_작성하지_않은_사람의_Id() {
        // given
        UUID otherUserId = UUID.randomUUID();
        
        // when & then
        assertThatThrownBy(() -> todo.validateAuthor(otherUserId))
                .isInstanceOf(AccessDeniedException.class);
    }
}