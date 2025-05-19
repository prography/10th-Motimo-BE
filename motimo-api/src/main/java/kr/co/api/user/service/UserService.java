package kr.co.api.user.service;

import kr.co.domain.user.dto.UserInfo;
import kr.co.domain.user.exception.DuplicateEmailException;
import kr.co.domain.user.exception.InvalidPasswordException;
import kr.co.domain.user.model.User;
import kr.co.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void create(String email, String nickname, String password) {
        if (userRepository.existsByEmail(email)) throw new DuplicateEmailException();
        User user = User.builder()
                .email(email)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .build();
        userRepository.save(user);
    }

    public UserInfo getUserByEmailAndPassword(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException();
        }

        return UserInfo.from(user);
    }
}
