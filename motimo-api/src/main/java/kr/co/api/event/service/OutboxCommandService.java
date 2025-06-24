package kr.co.api.event.service;

import kr.co.domain.common.outbox.OutboxEvent;
import kr.co.domain.common.outbox.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OutboxCommandService {

    private final OutboxEventRepository outboxEventRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createOutboxEvent(OutboxEvent outboxEvent) {
        outboxEventRepository.create(outboxEvent);
    }
}
