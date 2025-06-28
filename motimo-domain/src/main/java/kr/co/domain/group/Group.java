package kr.co.domain.group;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Group {
    @Builder.Default()
    private UUID id = null;
    private UUID goalId;
    private String title;
    private LocalDateTime lastActiveDate;
}
