package kr.co.domain.reaction;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Reaction {
    @Builder.Default()
    private UUID id = null;
    private UUID senderId;
    private UUID targetId;
    private ReactionType type;
}
