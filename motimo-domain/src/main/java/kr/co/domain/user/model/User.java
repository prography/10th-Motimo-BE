package kr.co.domain.user.model;

public record User(
        Long id,
        String email,
        String nickname,
        String password
) {
}
