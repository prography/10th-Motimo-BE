package kr.co.api.event.scheduler;

import java.util.List;
import kr.co.api.event.handler.OutboxEventHandlerRegistry;
import kr.co.api.group.service.GroupMessageCommandService;
import kr.co.domain.common.outbox.OutboxEvent;
import kr.co.domain.common.outbox.OutboxEventRepository;
import kr.co.infra.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxEventScheduler {

    private final OutboxEventRepository outboxEventRepository;
    private final StorageService storageService;
    private final GroupMessageCommandService groupMessageCommandService;
    private final OutboxEventHandlerRegistry outboxEventHandlerRegistry;

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void processOutboxEvents() {
        List<OutboxEvent> events = outboxEventRepository.findTop100ByOrderByCreatedAtAsc();

        for (OutboxEvent event : events) {
            try {
                outboxEventHandlerRegistry.dispatch(event.getEventType(), event.getPayload());
                outboxEventRepository.deleteById(event.getId());
            } catch (Exception ex) {
                log.error("Outbox 처리 실패 id={} type={}", event.getId(), event.getEventType(), ex);
            }
        }
    }
}
