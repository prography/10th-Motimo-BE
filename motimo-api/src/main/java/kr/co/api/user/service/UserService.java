package kr.co.api.user.service;

import kr.co.domain.auth.oauth2.OAuth2UserInfo;
import kr.co.domain.user.model.Provider;
import kr.co.domain.user.model.Role;
import kr.co.domain.user.model.User;
import kr.co.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User loginFromOAuth2(OAuth2UserInfo oAuth2UserInfo, String registrationId) {
        if (userRepository.existsByEmail(oAuth2UserInfo.getEmail())) {
            User existingUser = userRepository.findByEmail(oAuth2UserInfo.getEmail());
            return updateExistingUser(existingUser, oAuth2UserInfo);
        }
        User newUser = registerUserFromOAuth2(oAuth2UserInfo, registrationId);
        return userRepository.save(newUser);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findById(Long id) {
        return userRepository.findById(id);
    }

    private User registerUserFromOAuth2(OAuth2UserInfo oAuth2UserInfo, String registrationId) {
        return User.builder()
                .nickname(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .profileImageUrl(oAuth2UserInfo.getImageUrl())
                .role(Role.USER)
                .provider(Provider.valueOf(registrationId.toUpperCase()))
                .providerId(oAuth2UserInfo.getId())
                .build();
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oauth2UserInfo) {
        existingUser.updateProfile(oauth2UserInfo.getName(), oauth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }
}
