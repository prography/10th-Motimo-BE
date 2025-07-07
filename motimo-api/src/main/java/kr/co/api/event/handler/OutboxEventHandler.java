package kr.co.api.event.handler;

public interface OutboxEventHandler<T> {

    Class<T> payloadType();

    void handle(T event) throws Exception;

    default String eventType() {
        return payloadType().getSimpleName();
    }
}
