package kr.co.domain.user.dto;

import kr.co.domain.user.model.User;

public record UserInfo(Long id, String email, String nickname) {
    public static UserInfo from(User user) {
        return new UserInfo(user.getId(), user.getEmail(), user.getNickname());
    }
}
