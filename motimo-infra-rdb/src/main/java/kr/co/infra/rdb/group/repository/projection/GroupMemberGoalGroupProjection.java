package kr.co.infra.rdb.group.repository.projection;

import java.time.LocalDateTime;
import java.util.UUID;

public interface GroupMemberGoalGroupProjection {
    UUID getGoalId();
    String getGoalTitle();
    UUID getGroupId();
    LocalDateTime getGroupFinishedDate();
}