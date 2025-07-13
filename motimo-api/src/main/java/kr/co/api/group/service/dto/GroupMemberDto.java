package kr.co.api.group.service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record GroupMemberDto(
        UUID memberId,
        String nickname,
        LocalDateTime lastOnlineDate,
        Boolean isActivePoke
) {

}
