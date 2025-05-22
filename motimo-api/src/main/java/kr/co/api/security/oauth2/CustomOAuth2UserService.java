package kr.co.api.security.oauth2;

import kr.co.api.auth.service.AuthService;
import kr.co.api.security.UserPrincipal;
import kr.co.api.user.service.UserService;
import kr.co.domain.auth.oauth2.OAuth2UserInfo;
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

    private final AuthService authService;
    private final UserService userService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory
                .getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());

        authService.processOAuth2Login(oAuth2UserInfo, registrationId);
        User user = userService.findByEmail(oAuth2UserInfo.getEmail());

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }
}
