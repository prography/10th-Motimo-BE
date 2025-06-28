package kr.co.api.group.rqrs;

import java.time.LocalDateTime;

public record JoinedGroupRs(
        String title,
        LocalDateTime lastActiveDate,
        boolean isNotificationActive
) {
}
