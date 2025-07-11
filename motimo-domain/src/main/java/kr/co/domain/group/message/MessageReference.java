package kr.co.domain.group.message;

import java.util.UUID;

public record MessageReference(
        MessageReferenceType messageReferenceType,
        UUID referenceId
) {

}
