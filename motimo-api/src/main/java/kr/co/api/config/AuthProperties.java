package kr.co.api.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth")
@Getter
@RequiredArgsConstructor
public class AuthProperties {
    private final String jwtSecret;
    private final Long tokenExpiration;
    private final String issuer;
}
