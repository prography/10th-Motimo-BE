package kr.co.domain.user.repository;

import kr.co.domain.user.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    User save(User user);
}
