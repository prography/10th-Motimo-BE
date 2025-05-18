package kr.co.api.user.service;

import kr.co.domain.user.User;
import kr.co.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserApplicationService {
    private final UserRepository userRepository;

    @Transactional
    public User createUser(String username, String password) {
        User user = User.of(username, password);
        return userRepository.save(user);
    }
}
