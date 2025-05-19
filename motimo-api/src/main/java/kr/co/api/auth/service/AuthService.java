package kr.co.api.auth.service;

import kr.co.api.security.TokenProvider;
import kr.co.api.user.service.UserService;
import kr.co.domain.auth.dto.AuthInfo;
import kr.co.domain.user.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    @Transactional
    public void register(String email, String nickname, String password) {
        userService.create(email, nickname, password);
    }

    @Transactional(readOnly = true)
    public AuthInfo login(String email, String password) {
        UserInfo userInfo = userService.getUserByEmailAndPassword(email, password);
        return tokenProvider.createToken(userInfo.id(), userInfo.email());
    }
}
