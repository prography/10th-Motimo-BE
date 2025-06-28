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
    private UUID senderId;
    private UUID targetId;
    private String senderName;
    private GroupMessageType messageType;
    private String content;
    private List<Reaction> reactions;
    private LocalDateTime sendAt;
}
