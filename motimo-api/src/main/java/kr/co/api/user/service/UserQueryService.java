package kr.co.api.user.service;

import kr.co.domain.user.model.ProviderType;
import kr.co.domain.user.model.User;
import kr.co.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {
    private final UserRepository userRepository;

    public User findById(UUID id) {
        return userRepository.findById(id);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByEmailAndProviderType(String email, ProviderType providerType) {
        return userRepository.findByEmailAndProviderType(email, providerType);
    }

    public boolean existsByEmailAndProviderType(String email, ProviderType providerType) {
        return userRepository.existsByEmailAndProviderType(email, providerType);
    }
}
