package kr.co.api.user;

public record UserCreateRequest(
        String username,
        String password
) {
}
