package kr.co.domain.notification;

import lombok.Getter;

@Getter
public enum NotificationType {
    REACTION("${name}님이 리액션을 남겼습니다."),
    POKE("${name}님이 찌르기를 했어요!"),

    TODO_DUE_DAY("\"${todoTitle}\" 투두가 ${day}일 남았어요!"),
    GROUP_TODO_COMPLETED("${name}님이 투두를 완료했어요!"),
    GROUP_TODO_RESULT_COMPLETED("${name}님이 투두 기록을 남겼어요!")
    ;

    private final String defaultTitle;

    NotificationType(String defaultTitle) {
        this.defaultTitle = defaultTitle;
    }
}
