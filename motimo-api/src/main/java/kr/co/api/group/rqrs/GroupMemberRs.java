package kr.co.api.group.rqrs;

import java.time.LocalDateTime;
import java.util.UUID;

public record GroupMemberRs(
        UUID userId,
        String nickname,
        LocalDateTime lastOnlineDate,
        boolean isActivePock
) {

}
