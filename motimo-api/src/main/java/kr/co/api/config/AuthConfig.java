package kr.co.api.config;

import kr.co.api.security.jwt.JwtProperties;
import kr.co.api.security.oauth2.OAuth2Properties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({JwtProperties.class, OAuth2Properties.class})
public class AuthConfig {

}
