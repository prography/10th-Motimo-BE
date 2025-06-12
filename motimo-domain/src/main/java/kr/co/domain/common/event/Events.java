package kr.co.domain.common.event;

import org.springframework.context.ApplicationEventPublisher;

public class Events {

    private static ApplicationEventPublisher publisher;

    public static void setPublisher(ApplicationEventPublisher publisher) {
        Events.publisher = publisher;
    }

    public static void publishEvent(Event event) {
        if (publisher != null) {
            publisher.publishEvent(event);
        }
    }
}
