package kr.co.api.security.oauth2;

import kr.co.domain.auth.oauth2.GoogleOAuth2UserInfo;
import kr.co.domain.auth.oauth2.KakaoOAuth2UserInfo;
import kr.co.domain.auth.oauth2.OAuth2UserInfo;
import kr.co.domain.user.model.Provider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    protected static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(Provider.GOOGLE.name())) {
            return new GoogleOAuth2UserInfo(attributes);
        }
        else if (registrationId.equalsIgnoreCase(Provider.KAKAO.name())) {
            return new KakaoOAuth2UserInfo(attributes);
        }
        else {
            throw new IllegalArgumentException(registrationId + " is not supported yet.");
        }
    }

}
