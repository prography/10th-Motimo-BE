package kr.co.domain.group.reaction;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ReactionType {
    GOOD("굿"),
    COOL("멋져"),
    CHEER_UP("힘내"),
    BEST("최고"),
    LIKE("좋아요");

    private final String description;

    public String getDescription() {
        return description;
    }
}
