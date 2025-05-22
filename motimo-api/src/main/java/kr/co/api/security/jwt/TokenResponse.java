package kr.co.api.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        @JsonIgnore
        UUID tokenId
) {
}
