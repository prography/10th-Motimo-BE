package kr.co.infra.storage.exception;

import kr.co.domain.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StorageErrorCode implements ErrorCode {
    FILE_UPLOAD_FAILED("파일 업로드에 실패했습니다.", 500),
    FILE_DELETE_FAILED("파일 삭제에 실패했습니다.", 500);

    private final String message;
    private final int statusCode;
}
