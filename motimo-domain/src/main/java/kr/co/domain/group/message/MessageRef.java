package kr.co.domain.group.message;

import java.util.UUID;

public record MessageRef(
        MessageRefType messageRefType,
        UUID referenceId
) {

}
