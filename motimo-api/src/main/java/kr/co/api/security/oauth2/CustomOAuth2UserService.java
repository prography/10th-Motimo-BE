package kr.co.api.security.oauth2;

import kr.co.api.security.UserPrincipal;
import kr.co.api.user.service.UserCommandService;
import kr.co.api.user.service.UserQueryService;
import kr.co.domain.auth.oauth2.OAuth2UserInfo;
import kr.co.domain.user.model.ProviderType;
import kr.co.domain.user.model.Role;
import kr.co.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory
                .getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());

        String email = oAuth2UserInfo.getEmail();
        ProviderType providerType = ProviderType.of(registrationId.toUpperCase());

        User user = userQueryService.existsByEmailAndProviderType(email, providerType)
                ? userQueryService.findByEmailAndProviderType(email, providerType)
                : registerNewUser(oAuth2UserInfo, providerType);

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserInfo oAuth2UserInfo, ProviderType providerType) {
        User newUser = User.builder()
                .nickname(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .role(Role.USER)
                .providerType(providerType)
                .providerId(oAuth2UserInfo.getId())
                .build();

        return userCommandService.register(newUser);
    }
}
