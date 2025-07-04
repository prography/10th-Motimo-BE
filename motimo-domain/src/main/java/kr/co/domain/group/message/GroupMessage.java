package kr.co.domain.group.message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.co.domain.group.reaction.Reaction;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupMessage {

    @Builder.Default
    private UUID id = null;
    private UUID groupId;
    private UUID userId;
    private GroupMessageType messageType;
    private MessageReference messageReference;
    private List<Reaction> reactions;
    @Builder.Default
    private LocalDateTime sendAt = LocalDateTime.now();

    public int getReactionCount() {
        return reactions.size();
    }

    @Builder(builderMethodName = "createGroupMessage")
    private GroupMessage(UUID groupId, UUID userId, String userName, GroupMessageType messageType,
            MessageReference messageReference) {
        this.groupId = groupId;
        this.userId = userId;
        this.messageType = messageType;
        this.messageReference = messageReference;
    }
}
