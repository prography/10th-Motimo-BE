package kr.co.infra.rdb.todo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kr.co.domain.todo.Emotion;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TodoResultEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "emotion")
    private Emotion emotion;

    @Column(name = "result_content")
    private String resultContent;

    @Column(name = "result_image_url")
    private String resultImageUrl;
}
