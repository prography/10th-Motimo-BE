package kr.co.domain.group.exception;

import kr.co.domain.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GroupErrorCode implements ErrorCode {
    GROUP_NOT_FOUND("group를 찾을 수 없습니다.", 404),
    ALREADY_JOINED_GROUP("이미 그룹에 참여중입니다.", 409),
    MESSAGE_LOAD_FAIL("메시지를 로드하는데 실패했습니다.", 500),
    UNSUPPORTED_MESSAGE_TYPE("지원하지 않는 메시지 타입입니다.", 400),

    USER_NOT_IN_GROUP("해당 그룹에 존재하지 않는 사용자입니다.", 404),

    ALREADY_TODAY_POKE("오늘 찌르기를 이미 완료했습니다.", 400);

    private final String message;
    private final int statusCode;
}
