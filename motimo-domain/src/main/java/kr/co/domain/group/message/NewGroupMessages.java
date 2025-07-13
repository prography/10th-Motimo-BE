package kr.co.domain.group.message;

import java.time.LocalDateTime;
import java.util.UUID;

public record NewGroupMessages(
        int count,
        LocalDateTime latestTime,
        UUID latestMessageId
) {

}
