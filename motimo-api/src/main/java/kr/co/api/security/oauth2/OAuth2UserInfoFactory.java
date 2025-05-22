package kr.co.api.security.oauth2;

import kr.co.domain.auth.oauth2.GoogleOAuth2UserInfo;
import kr.co.domain.auth.oauth2.KakaoOAuth2UserInfo;
import kr.co.domain.auth.oauth2.OAuth2UserInfo;
import kr.co.domain.user.model.ProviderType;

import java.util.Map;

public class OAuth2UserInfoFactory {

    protected static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(ProviderType.GOOGLE.name())) {
            return new GoogleOAuth2UserInfo(attributes);
        }
        else if (registrationId.equalsIgnoreCase(ProviderType.KAKAO.name())) {
            return new KakaoOAuth2UserInfo(attributes);
        }
        else {
            throw new IllegalArgumentException(registrationId + " is not supported yet.");
        }
    }

}
