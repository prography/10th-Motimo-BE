package kr.co.domain.user.model;

import java.util.UUID;

public record User(
        UUID id,
        String email,
        String nickname,
        String password
) {
}
