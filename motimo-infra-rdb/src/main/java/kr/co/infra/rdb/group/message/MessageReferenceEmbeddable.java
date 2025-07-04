package kr.co.infra.rdb.group.message;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.UUID;
import kr.co.domain.group.message.MessageReference;
import kr.co.domain.group.message.MessageReferenceType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageReferenceEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type")
    private MessageReferenceType messageRefType;

    @Column(name = "reference_id")
    private UUID referenceId;

    public static MessageReferenceEmbeddable from(MessageReference messageReference) {
        return new MessageReferenceEmbeddable(messageReference.messageRefType(),
                messageReference.referenceId());
    }

    public MessageReference toDomain() {
        return new MessageReference(messageRefType, referenceId);
    }
}
