package kr.co.api.event.listener;

import kr.co.api.user.service.UserCommandService;
import kr.co.domain.common.event.UserLoginEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventListener {
    private final UserCommandService userCommandService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserLogin(UserLoginEvent event) {
        try {
            userCommandService.updateUserLoginAt(event.getUserId());
        } catch (Exception e) {
            log.error("유저 로그인 시간 업데이트 실패 {}:", e.getMessage());
        }
    }
}
