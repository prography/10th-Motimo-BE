package kr.co.domain.group.message;

public enum GroupMessageType {
    JOIN("그룹 입장"),
    LEAVE("그룹 퇴장"),

    TODO_COMPLETE("투두 완료"),
    TODO_RESULT_SUBMIT("투두 결과 제출"),

    MESSAGE_REACTION("메시지 리액션");

    private final String description;

    GroupMessageType(String description) {
        this.description = description;
    }
}
