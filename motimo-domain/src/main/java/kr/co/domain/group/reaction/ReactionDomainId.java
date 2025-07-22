package kr.co.domain.group.reaction;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ReactionDomainId {
    private UUID userId;
    private UUID messageId;
}
