package kr.co.api.event.handler;

import kr.co.domain.common.event.FileRollbackEvent;
import kr.co.infra.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileRollbackEventHandler implements OutboxEventHandler<FileRollbackEvent> {

    private final StorageService storage;

    @Override
    public Class<FileRollbackEvent> payloadType() {
        return FileRollbackEvent.class;
    }

    @Override
    public void handle(FileRollbackEvent event) {
        storage.delete(event.getFilePath());
    }
}
