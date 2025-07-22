package kr.co.infra.rdb.group.reaction;

import java.io.Serializable;
import java.util.UUID;
import kr.co.domain.group.reaction.ReactionDomainId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class ReactionId implements Serializable {
    private UUID userId;
    private UUID messageId;

    public static ReactionId from(ReactionDomainId domainId) {
        return new ReactionId(
                domainId.getUserId(),
                domainId.getMessageId()
        );
    }
}
