package kr.co.domain.common.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.domain.common.event.Event;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OutboxEvent {

    private UUID id;
    private String eventType;
    private String payload;
    private LocalDateTime createdAt;

    public static OutboxEvent from(Event event) {
        try {
            return new OutboxEvent(
                    event.getClass().getSimpleName(),
                    new ObjectMapper().writeValueAsString(event)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }

    protected OutboxEvent(String eventType, String payload) {
        this.eventType = eventType;
        this.payload = payload;
        this.createdAt = LocalDateTime.now();
    }
}
