package kr.co.domain.group.reaction;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reaction {
    private ReactionDomainId id;
    private ReactionType reactionType;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder(builderMethodName = "createReaction")
    private Reaction(UUID userId, UUID messageId, ReactionType reactionType) {
        this.id = ReactionDomainId.of(userId, messageId);
        this.reactionType = reactionType;
    }

    public UUID getUserId() {
        return id.getUserId();
    }

    public UUID getMessageId() {
        return id.getMessageId();
    }
}
