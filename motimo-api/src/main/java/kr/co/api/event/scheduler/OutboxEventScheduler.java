package kr.co.api.event.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kr.co.api.group.service.GroupMessageCommandService;
import kr.co.domain.common.event.FileDeletedEvent;
import kr.co.domain.common.event.FileRollbackEvent;
import kr.co.domain.common.event.group.message.GroupMessageDeletedEvent;
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

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void processOutboxEvents() {
        List<OutboxEvent> events = outboxEventRepository.findTop100ByOrderByCreatedAtAsc();
        for (OutboxEvent event : events) {
            try {
                if ("FileRollbackEvent".equals(event.getEventType())) {
                    FileRollbackEvent upload = new ObjectMapper().readValue(event.getPayload(),
                            FileRollbackEvent.class);
                    storageService.delete(upload.getFilePath());

                } else if ("FileDeletedEvent".equals(event.getEventType())) {
                    FileDeletedEvent delete = new ObjectMapper().readValue(event.getPayload(),
                            FileDeletedEvent.class);
                    storageService.delete(delete.getFilePath());
                } else if ("GroupMessageDeletedEvent".equals(event.getEventType())) {
                    GroupMessageDeletedEvent delete = new ObjectMapper().readValue(
                            event.getPayload(),
                            GroupMessageDeletedEvent.class);
                    groupMessageCommandService.deleteAllByReferenceId(delete.getReferenceId());
                }
                outboxEventRepository.deleteById(event.getId());
            } catch (Exception e) {
                log.error("Outbox 처리 실패: {}", event, e);
            }
        }
    }
}
