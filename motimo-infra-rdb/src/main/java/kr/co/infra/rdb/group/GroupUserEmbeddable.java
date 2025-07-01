package kr.co.infra.rdb.group;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class GroupUserEmbeddable {
    private UUID userId;
    private LocalDateTime joinedDate;
    private boolean isExited;
}
