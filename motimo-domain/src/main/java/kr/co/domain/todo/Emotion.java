package kr.co.domain.todo;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;

public enum Emotion {

    PROUD("뿌듯"),
    REGRETFUL("아쉬움"),
    IMMERSED("몰입"),
    SELF_REFLECTION("성찰"),
    ROUTINE("루틴");

    private final String value;

    Emotion(String value) {
        this.value = value;
    }

    @JsonCreator
    public static Emotion from(String value) {
        return Arrays.stream(values())
                .filter(e -> e.value.equals(value) || e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid emotion: " + value));
    }
}

