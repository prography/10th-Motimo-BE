package kr.co.api.group.service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record JoinedGroupDto(
        UUID groupId,
        String title,
        LocalDateTime lastActiveDate,
        boolean isNotificationAction
) {

}
