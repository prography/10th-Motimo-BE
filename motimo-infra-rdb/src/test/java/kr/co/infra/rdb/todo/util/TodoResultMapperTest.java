package kr.co.infra.rdb.todo.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.domain.todo.Emotion;
import kr.co.domain.todo.TodoResult;
import kr.co.infra.rdb.todo.entity.TodoResultEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TodoResultMapper 테스트")
class TodoResultMapperTest {

    @Test
    void 투두_결과_엔티티에서_도메인으로_변환() {
        // given
        UUID id = UUID.randomUUID();
        UUID todoId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        TodoResultEntity entity = TodoResultEntity.builder()
                .id(id)
                .todoId(todoId)
                .emotion(Emotion.PROUD)
                .content("투두 완료!")
                .filePath("/image.jpg")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // when
        TodoResult result = TodoResultMapper.toDomain(entity);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getTodoId()).isEqualTo(todoId);
        assertThat(result.getEmotion()).isEqualTo(Emotion.PROUD);
        assertThat(result.getContent()).isEqualTo("투두 완료!");
        assertThat(result.getFilePath()).isEqualTo("/image.jpg");
        assertThat(result.getCreatedAt()).isEqualTo(now);
        assertThat(result.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void 투두_결과_도메인에서_엔티티로_변환() {
        // given
        UUID id = UUID.randomUUID();
        UUID todoId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        TodoResult domain = TodoResult.builder()
                .id(id)
                .todoId(todoId)
                .emotion(Emotion.PROUD)
                .content("투두 완료!")
                .filePath("/image.jpg")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // when
        TodoResultEntity entity = TodoResultMapper.toEntity(domain);

        // then
        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getTodoId()).isEqualTo(todoId);
        assertThat(entity.getEmotion()).isEqualTo(Emotion.PROUD);
        assertThat(entity.getContent()).isEqualTo("투두 완료!");
        assertThat(entity.getFilePath()).isEqualTo("/image.jpg");
        assertThat(entity.getCreatedAt()).isEqualTo(now);
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
    }
}
