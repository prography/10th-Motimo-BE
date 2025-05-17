package kr.co.api.user;

import kr.co.domain.user.User;

public record UserResponse(
        Long id, String username, String password
) {
    public static UserResponse ofUser(User user) {
        return new UserResponse(user.id(), user.username(), user.password());
    }
}
