package kr.co.domain.common.event.group.message;

import java.util.UUID;
import kr.co.domain.common.event.Event;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupMessageDeletedEvent extends Event {

    private UUID referenceId;

    public GroupMessageDeletedEvent(UUID referenceId) {
        this.referenceId = referenceId;
    }
}
