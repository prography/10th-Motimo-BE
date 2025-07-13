package kr.co.api.event.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OutboxEventHandlerRegistry {

    private final ObjectMapper objectMapper;
    private final Map<String, OutboxEventHandler<?>> handlers;

    public OutboxEventHandlerRegistry(List<OutboxEventHandler<?>> list, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        // 모든 핸들러 Bean 을 eventType → 인스턴스 Map 으로 변환
        // ex) key = "FileRollbackEvent", value = "FileRollbackEventHandler"
        this.handlers = list.stream()
                .collect(Collectors.toMap(
                        handler -> handler.payloadType().getName(),
                        handler -> handler));
    }

    // 이벤트 타입에 맞는 핸들러를 찾아 실행
    public void dispatch(String eventType, String json) throws Exception {
        OutboxEventHandler<?> outboxEventHandler = handlers.get(eventType);
        if (outboxEventHandler == null) {
            throw new IllegalStateException("No handler for " + eventType);
        }

        Object payload = objectMapper.readValue(json, outboxEventHandler.payloadType());

        @SuppressWarnings("unchecked")
        OutboxEventHandler<Object> handler = (OutboxEventHandler<Object>) outboxEventHandler;
        handler.handle(payload);
    }
}
