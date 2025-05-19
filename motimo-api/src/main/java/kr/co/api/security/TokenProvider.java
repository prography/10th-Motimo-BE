package kr.co.api.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.co.api.config.AuthProperties;
import kr.co.domain.auth.dto.AuthInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private final AuthProperties authProperties;
    private final SecretKey secretKey;
    private final JwtParser jwtParser;

    public TokenProvider(AuthProperties authProperties) {
        this.authProperties = authProperties;
        byte[] keyBytes = Decoders.BASE64URL.decode(authProperties.getJwtSecret());
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.jwtParser = Jwts.parser().verifyWith(secretKey).build();
    }

    public AuthInfo createToken(Long id, String email) {
        String accessToken = createAccessToken(id, email);
        // todo: refresh token
        return new AuthInfo(accessToken);
    }

    // valid token
    public boolean validateToken(String token) {
        try {
            jwtParser.parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Invalid token: {}", e.getMessage());
            return false;
        }
    }

    public Long getIdFromToken(String token) {
        return jwtParser.parseSignedClaims(token)
                .getPayload()
                .get("userId", Long.class);
    }

    private String createAccessToken(Long id, String email) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + authProperties.getTokenExpiration());

        return Jwts.builder()
                .subject(id.toString())
                .issuer(authProperties.getIssuer())
                .issuedAt(now)
                .expiration(expiredDate)
                .claim("userId", id)
                .claim("email", email)
                .signWith(secretKey)
                .compact();
    }
}