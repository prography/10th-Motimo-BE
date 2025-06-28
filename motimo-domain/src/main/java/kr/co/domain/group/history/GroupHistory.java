package kr.co.domain.group.history;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.co.domain.reaction.Reaction;
import kr.co.domain.user.model.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupHistory {
    @Builder.Default()
    private UUID id = null;
    private GroupHistoryType historyType;
    private LocalDateTime createdAt;
    private User creator;
    private List<Reaction> reactions;
}
