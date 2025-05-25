package kr.co.domain.user.repository;

import kr.co.domain.user.model.ProviderType;
import kr.co.domain.user.model.User;

import java.util.UUID;

public interface UserRepository {
    User findById(UUID id);
    User findByEmail(String email);
    User findByEmailAndProviderType(String email, ProviderType providerType);
    User save(User user);
    boolean existsByEmailAndProviderType(String email, ProviderType providerType);
}
