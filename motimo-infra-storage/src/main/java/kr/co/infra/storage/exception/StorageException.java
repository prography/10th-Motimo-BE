package kr.co.infra.storage.exception;

import kr.co.domain.common.exception.BusinessException;

public class StorageException extends BusinessException {

    public StorageException(StorageErrorCode errorCode) {
        super(errorCode);
    }
}
