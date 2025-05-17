package kr.co.api.user.service;

import kr.co.domain.user.User;
import kr.co.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserApplicationService {
    private final UserService userService;

    @Transactional
    public User createUser(String email, String password) {
        return userService.create(email, password);
    }
}
