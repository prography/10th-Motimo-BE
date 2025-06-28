package kr.co.domain.group.history;

public enum GroupHistoryType {
    ENTER("그룹 입장");

    private final String description;

    GroupHistoryType(String description) {
        this.description = description;
    }
}
