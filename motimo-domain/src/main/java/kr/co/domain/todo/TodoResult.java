package kr.co.domain.todo;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TodoResult {
    private Emotion emotion;
    private String resultContent;
    private String resultImageUrl;
}
