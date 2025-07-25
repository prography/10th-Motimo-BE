package kr.co.domain.group.message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import kr.co.domain.group.message.frozenData.FrozenData;
import kr.co.domain.group.message.frozenData.FrozenDataConverter;
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
    @Builder.Default
    private MessageReference messageReference = null;
    @Builder.Default
    private String staticJson = null;
    @Builder.Default
    private Map<String, String> frozenData = new HashMap<>();
    @Builder.Default
    private List<Reaction> reactions = new ArrayList<>();
    @Builder.Default
    private LocalDateTime sendAt = LocalDateTime.now();

    public int getReactionCount() {
        return reactions.size();
    }

    @Builder(builderMethodName = "createGroupMessage")
    private GroupMessage(UUID groupId, UUID userId, GroupMessageType messageType,
            MessageReference messageReference) {
        this.groupId = groupId;
        this.userId = userId;
        this.messageType = messageType;
        this.messageReference = messageReference;
    }

    public <T extends FrozenData> T getFrozenData(Class<T> type) {
        return FrozenDataConverter.convertTo(this.frozenData, type);
    }

    public <T extends FrozenData> void setFrozenData(T data) {
        this.frozenData = FrozenDataConverter.convertFrom(data);
    }
}
