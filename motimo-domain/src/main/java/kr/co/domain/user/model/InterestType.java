package kr.co.domain.user.model;

import lombok.Getter;

@Getter
public enum InterestType {
    HEALTH("건강"),
    READING("독서"),
    STUDY("학업"),
    LANGUAGE("어학"),
    SPORTS("운동"),
    PROGRAMMING("프로그래밍"),
    CAREER("취업/이직"),
    SELF_IMPROVEMENT("자기계발");

    private final String label;
    
    InterestType(String label) {
        this.label = label;
    }
}
