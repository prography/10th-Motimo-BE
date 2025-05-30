package kr.co.infra.rdb.todo.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.domain.todo.Emotion;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.TodoResult;
import kr.co.infra.rdb.todo.entity.TodoEntity;
import kr.co.infra.rdb.todo.entity.TodoResultEmbeddable;
import org.junit.jupiter.api.Test;

public class TodoMapperTest {

    @Test
    void 투두_엔티티에서_도메인_모델로_변환() {
        // given
        UUID id = UUID.randomUUID();
        UUID subGoalId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        TodoResultEmbeddable embeddable = TodoResultEmbeddable.builder()
                .emotion(Emotion.HAPPY)
                .resultContent("투두 끝!")
                .resultImageUrl("img")
                .build();

        TodoEntity entity = TodoEntity.builder()
                .id(id)
                .subGoalId(subGoalId)
                .authorId(authorId)
                .title("오늘의 투두")
                .date(LocalDate.of(2025, 1, 1))
                .completed(true)
                .result(embeddable)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // when
        Todo domain = TodoMapper.toDomain(entity);

        // then
        assertThat(domain.getId()).isEqualTo(id);
        assertThat(domain.getTitle()).isEqualTo(entity.getTitle());
        assertThat(domain.getResult().getEmotion()).isEqualTo(embeddable.getEmotion());
        assertThat(domain.getResult().getResultContent()).isEqualTo(embeddable.getResultContent());
        assertThat(domain.getResult().getResultImageUrl()).isEqualTo(
                embeddable.getResultImageUrl());
    }

    @Test
    void 투두_모델에서_엔티티로_변환() {
        // given
        UUID id = UUID.randomUUID();
        UUID subGoalId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        TodoResult todoResult = TodoResult.builder()
                .emotion(Emotion.HAPPY)
                .resultContent("투두 끝!")
                .resultImageUrl("img")
                .build();

        Todo domain = Todo.builder()
                .id(id)
                .subGoalId(subGoalId)
                .authorId(authorId)
                .title("오늘의 투두")
                .date(LocalDate.of(2025, 1, 1))
                .completed(true)
                .result(todoResult)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // when
        TodoEntity entity = TodoMapper.toEntity(domain);

        // then
        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getTitle()).isEqualTo(domain.getTitle());
        assertThat(entity.getResult().getEmotion()).isEqualTo(todoResult.getEmotion());
        assertThat(entity.getResult().getResultContent()).isEqualTo(todoResult.getResultContent());
        assertThat(entity.getResult().getResultImageUrl()).isEqualTo(
                todoResult.getResultImageUrl());
    }
}