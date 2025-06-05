package kr.co.infra.storage.exception;

import kr.co.domain.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StorageErrorCode implements ErrorCode {
    FILE_UPLOAD_FAILED("파일 업로드에 실패했습니다.", 500),
    FILE_DELETE_FAILED("파일 삭제에 실패했습니다.", 500),
    INVALID_FILE("파일이 없거나 비어 있습니다.", 400);

    private final String message;
    private final int statusCode;
}
