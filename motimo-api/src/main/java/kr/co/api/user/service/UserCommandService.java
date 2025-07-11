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
import org.springframework.util.StringUtils;
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

        String newProfilePath = resolveNewProfileImagePath(user, image);

        user.update(userName, bio, newProfilePath, interests);
        return userRepository.update(user).getId();
    }

    private String resolveNewProfileImagePath(User user, MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return user.getProfileImagePath();
        }

        return uploadNewProfileImage(user, image);
    }

    private String uploadNewProfileImage(User user, MultipartFile image) {
        String newImagePath = generateProfileImagePath(user.getId());

        storageService.store(image, newImagePath);
        Events.publishEvent(new FileRollbackEvent(newImagePath));

        if (StringUtils.hasText(user.getProfileImagePath())) {
            Events.publishEvent(new FileDeletedEvent(user.getProfileImagePath()));
        }
        return newImagePath;
    }

    private String generateProfileImagePath(UUID userId) {
        return "user/%s/%s".formatted(userId, UUID.randomUUID());
    }

}
