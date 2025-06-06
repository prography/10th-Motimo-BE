package kr.co.domain.common.event;

import java.time.Instant;
import lombok.Getter;

@Getter
public abstract class Event {

    private long timestamp;

    public Event() {
        this.timestamp = Instant.now().toEpochMilli();
    }
}
