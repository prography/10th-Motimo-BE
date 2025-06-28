package kr.co.domain.group.reaction;

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

    @Builder.Default
    private UUID id = null;
    private UUID senderId;
    private UUID targetId;
    private ReactionType reactionType;
}
