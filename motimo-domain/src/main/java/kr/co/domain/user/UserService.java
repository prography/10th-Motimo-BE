package kr.co.domain.user;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User create(String username, String password) {
        User user = new User(null, username, password);
        return userRepository.save(user);
    }
}
