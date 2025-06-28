package kr.co.api.security.oauth2;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.oauth2")
@Getter
@RequiredArgsConstructor
public class OAuth2Properties {

    private final String frontRedirectUrl;
}
