package kr.co.domain.group.history;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.co.domain.reaction.Reaction;
import kr.co.domain.user.model.User;

public class GroupHistory {
    private UUID id;
    private GroupHistoryType historyType;
    private LocalDateTime createdAt;
    private User creator;
    private List<Reaction> reactions;
}
