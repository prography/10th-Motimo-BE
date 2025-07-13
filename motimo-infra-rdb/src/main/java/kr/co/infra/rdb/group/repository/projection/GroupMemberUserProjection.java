package kr.co.infra.rdb.group.repository.projection;

import java.time.LocalDateTime;
import java.util.UUID;

public interface GroupMemberUserProjection {
    UUID getMemberId();
    UUID getGoalId();
    String getNickname();
    String getEmail();
    String getProfileImageUrl();
    LocalDateTime getJoinedDate();
    boolean isNotificationActive();
}
