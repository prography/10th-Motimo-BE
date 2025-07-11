package kr.co.api.event.handler;

import kr.co.domain.common.event.FileDeletedEvent;
import kr.co.infra.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileDeletedEventHandler implements OutboxEventHandler<FileDeletedEvent> {

    private final StorageService storage;

    @Override
    public Class<FileDeletedEvent> payloadType() {
        return FileDeletedEvent.class;
    }

    @Override
    public void handle(FileDeletedEvent e) {
        storage.delete(e.getFilePath());
    }
}
