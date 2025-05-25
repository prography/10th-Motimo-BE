package kr.co.domain.user.exception;

import kr.co.domain.common.exception.BusinessException;

public class UnsupportedProviderTypeException extends BusinessException {
    public UnsupportedProviderTypeException() {
        super(UserErrorCode.UNSUPPORTED_PROVIDER_TYPE);
    }
}
