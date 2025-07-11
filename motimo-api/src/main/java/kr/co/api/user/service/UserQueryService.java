package kr.co.api.user.service;

import java.util.UUID;
import kr.co.api.user.dto.UserProfileDto;
import kr.co.domain.user.model.ProviderType;
import kr.co.domain.user.model.User;
import kr.co.domain.user.repository.UserRepository;
import kr.co.infra.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;
    private final StorageService storageService;

    public User findById(UUID id) {
        return userRepository.findById(id);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByEmailAndProviderType(String email, ProviderType providerType) {
        return userRepository.findByEmailAndProviderType(email, providerType);
    }

    public UserProfileDto getProfile(UUID userId) {
        User user = userRepository.findById(userId);
        String profileUrl = resolveProfileImageUrl(user.getProfileImageUrl());
        return UserProfileDto.of(user, profileUrl);
    }

    public boolean existsByEmailAndProviderType(String email, ProviderType providerType) {
        return userRepository.existsByEmailAndProviderType(email, providerType);
    }

    private String resolveProfileImageUrl(String path) {
        if (!StringUtils.hasText(path)) {
            return "";
        }
        return storageService.getFileUrl(path);
    }
}
