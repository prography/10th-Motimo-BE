package kr.co.api.event.listener;

import kr.co.domain.common.event.FileDeletedEvent;
import kr.co.domain.common.event.FileRollbackEvent;
import kr.co.infra.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileEventListener {

    private final StorageService storageService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleFileRollback(FileRollbackEvent event) {
        String filePath = event.getFilePath();
        try {
            storageService.delete(filePath);
        } catch (Exception e) {
            // todo: 삭제 실패 시 로깅만, 재시도 x => 추후 아웃박스 도입 고려)
            log.error("롤백 후 파일 삭제 실패 {}:", e.getMessage());
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleFileDelete(FileDeletedEvent event) {
        String filePath = event.getFilePath();
        try {
            storageService.delete(filePath);
        } catch (Exception e) {
            log.error("커밋 후 파일 삭제 실패 {}: {}", filePath, e.getMessage());
        }
    }

}