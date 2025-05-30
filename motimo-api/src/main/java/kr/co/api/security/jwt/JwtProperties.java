package kr.co.api.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.jwt")
@Getter
@RequiredArgsConstructor
public class JwtProperties {

    private final String jwtSecret;
    private final Long tokenExpiration;
    private final Long refreshTokenExpiration;
    private final String issuer;
}
