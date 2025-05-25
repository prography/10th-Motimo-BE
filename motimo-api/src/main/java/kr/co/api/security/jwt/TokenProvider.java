package kr.co.api.security.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;
    private final JwtParser jwtParser;

    public TokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        byte[] keyBytes = Decoders.BASE64URL.decode(jwtProperties.getJwtSecret());
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.jwtParser = Jwts.parser().verifyWith(secretKey).build();
    }

    public TokenResponse createToken(UUID userId) {
        UUID tokenId = UUID.randomUUID();
        String accessToken = createAccessToken(userId.toString(), tokenId.toString());
        String refreshToken = createRefreshToken(userId.toString(), tokenId.toString());
        return new TokenResponse(accessToken, refreshToken, tokenId);
    }

    public boolean validateToken(String token) {
        try {
            jwtParser.parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Invalid token: {}", e.getMessage());
            return false;
        }
    }

    public UUID getUserIdFromToken(String token) {
        String userId = jwtParser.parseSignedClaims(token)
                .getPayload()
                .get("userId", String.class);

        try {
            return UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid User UUID Id format in token: {}", userId);
            throw new IllegalArgumentException("토큰에서 유효하지 않은 UUID를 찾았습니다: " + userId, e);
        }
    }

    public UUID getTokenIdFromToken(String token) {
        String tokenId = jwtParser.parseSignedClaims(token)
                .getPayload()
                .get("tokenId", String.class);

        try {
            return UUID.fromString(tokenId);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid Token UUID Id format in token: {}", tokenId);
            throw new IllegalArgumentException("토큰에서 유효하지 않은 UUID를 찾았습니다: " + tokenId, e);
        }
    }

    private String createAccessToken(String userId, String tokenId) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + jwtProperties.getTokenExpiration());

        return Jwts.builder()
                .subject(userId)
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiration(expiredDate)
                .claim("userId", userId)
                .claim("tokenId", tokenId)
                .signWith(secretKey)
                .compact();
    }

    private String createRefreshToken(String userId, String tokenId) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + jwtProperties.getRefreshTokenExpiration());

        return Jwts.builder()
                .subject(userId)
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiration(expiredDate)
                .claim("userId", userId)
                .claim("tokenId", tokenId)
                .signWith(secretKey)
                .compact();
    }
}