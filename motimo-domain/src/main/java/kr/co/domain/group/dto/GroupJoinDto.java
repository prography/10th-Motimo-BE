package kr.co.domain.group.dto;

import java.util.UUID;
import lombok.Builder;

@Builder
public record GroupJoinDto(
        UUID groupId,
        UUID userId,
        UUID goalId
) {
}
