package kr.co.domain.group.message;

import java.time.LocalDateTime;
import java.util.UUID;
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
    private String userName;
    private MessageReference messageReference;
    private GroupMessageContent content;
    @Builder.Default
    private LocalDateTime sendAt = LocalDateTime.now();

    @Builder(builderMethodName = "createGroupMessage")
    private GroupMessage(UUID groupId, UUID userId, String userName,
            MessageReference messageReference, GroupMessageContent content) {
        this.groupId = groupId;
        this.userId = userId;
        this.userName = userName;
        this.messageReference = messageReference;
        this.content = content;
    }
}
