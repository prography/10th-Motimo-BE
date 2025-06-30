package kr.co.domain.group.message;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private String senderName;
    private GroupMessageContent content;
    @Builder.Default
    private List<Reaction> reactions = new ArrayList<>();
    @Builder.Default
    private LocalDateTime sendAt = LocalDateTime.now();
}
