package kr.co.domain.notification;

import lombok.Getter;

@Getter
public enum NotificationType {
    POKE("찌르기 제목", "찌르기 컨텐츠");

    private final String defaultTitle;
    private final String defaultContent;

    NotificationType(String defaultTitle, String defaultContent) {
        this.defaultTitle = defaultTitle;
        this.defaultContent = defaultContent;
    }
}
