package kr.co.domain.common.exception;

public class AccessDeniedException extends BusinessException {
    public AccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
