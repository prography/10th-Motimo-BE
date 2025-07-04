package kr.co.domain.group.message;

import java.util.UUID;

public record MessageReference(
        MessageReferenceType messageRefType,
        UUID referenceId
) {

}
