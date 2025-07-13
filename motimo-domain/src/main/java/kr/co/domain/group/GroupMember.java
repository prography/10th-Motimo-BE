package kr.co.domain.group;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupMember {
    private UUID memberId;
    private String nickname;
    private String email;
    private String profileImageUrl;
    private UUID goalId;
    private LocalDateTime joinedDate;
    private LocalDateTime lastOnlineDate;
    private boolean isNotificationActive;
}
