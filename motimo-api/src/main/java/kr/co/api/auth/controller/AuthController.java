package kr.co.api.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import kr.co.api.auth.rqrs.TokenReissueRq;
import kr.co.api.auth.service.AuthService;
import kr.co.api.security.annotation.AuthToken;
import kr.co.api.security.jwt.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "토큰 재발급", description = "refresh token을 통한 토큰 재발급 API")
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(@RequestBody TokenReissueRq request) {
        TokenResponse tokenResponse = authService.reissueToken(request.refreshToken());
        return ResponseEntity.ok(tokenResponse);
    }

    @Operation(summary = "로그아웃", description = "사용자 로그아웃 API")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthToken UUID tokenId) {
        authService.logout(tokenId);
        return ResponseEntity.ok().build();
    }
}
