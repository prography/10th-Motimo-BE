package kr.co.api.auth;

import jakarta.validation.Valid;
import kr.co.api.auth.dto.AuthResponse;
import kr.co.api.auth.dto.LoginRequest;
import kr.co.api.auth.dto.SignUpRequest;
import kr.co.api.auth.service.AuthService;
import kr.co.domain.auth.dto.AuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> signup(@Valid @RequestBody SignUpRequest request) {
        authService.register(request.email(), request.nickname(), request.password());
        return ResponseEntity.created(URI.create("")).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthInfo authInfo = authService.login(request.email(), request.password());
        return ResponseEntity.ok(new AuthResponse(authInfo.accessToken()));
    }

}
