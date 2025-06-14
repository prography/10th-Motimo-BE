package kr.co.domain.todo;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;

public enum TodoStatus {
    COMPLETE,
    INCOMPLETE;

    @JsonCreator
    public static TodoStatus from(String value) {
        return Arrays.stream(values())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid status: " + value));
    }
}
