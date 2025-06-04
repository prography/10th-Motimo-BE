package kr.co.domain.todo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

public enum Emotion {

    PROUD("뿌듯"),
    REGRETFUL("아쉬움"),
    IMMERSED("몰입"),
    GROWN("성장");

    private final String value;

    Emotion(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Emotion from(String value) {
        return Arrays.stream(values())
                .filter(e -> e.value.equals(value) || e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid emotion: " + value));
    }
}

