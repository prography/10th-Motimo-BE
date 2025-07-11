package kr.co.api.user.service;

import java.util.Set;
import java.util.UUID;
import kr.co.domain.common.event.Events;
import kr.co.domain.common.event.FileDeletedEvent;
import kr.co.domain.common.event.FileRollbackEvent;
import kr.co.domain.user.model.InterestType;
import kr.co.domain.user.model.User;
import kr.co.domain.user.repository.UserRepository;
import kr.co.infra.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;
    private final StorageService storageService;

    public User register(User user) {
        return userRepository.create(user);
    }

    public UUID updateInterests(UUID userId, Set<InterestType> interests) {
        User user = userRepository.findById(userId);
        user.updateInterests(interests);
        return userRepository.update(user).getId();
    }

    public UUID updateProfile(UUID userId, String userName, String bio, Set<InterestType> interests,
            MultipartFile image) {

        User user = userRepository.findById(userId);

        String profileUrl = user.getProfileImageUrl();
        if (image != null && !image.isEmpty()) {
            String newProfilePath = String.format("user/%s/%s", userId, UUID.randomUUID());
            storageService.store(image, newProfilePath);
            Events.publishEvent(new FileRollbackEvent(newProfilePath));
            if (profileUrl != null && !profileUrl.isBlank()) {
                Events.publishEvent(new FileDeletedEvent(profileUrl));
            }
            profileUrl = newProfilePath;
        }

        user.update(userName, bio, profileUrl, interests);
        return userRepository.update(user).getId();
    }
}
