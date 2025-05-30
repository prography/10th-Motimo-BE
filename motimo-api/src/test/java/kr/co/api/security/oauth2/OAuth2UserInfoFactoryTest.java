package kr.co.api.security.oauth2;

import kr.co.domain.auth.oauth2.GoogleOAuth2UserInfo;
import kr.co.domain.auth.oauth2.KakaoOAuth2UserInfo;
import kr.co.domain.auth.oauth2.OAuth2UserInfo;
import kr.co.domain.user.exception.UnsupportedProviderTypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class OAuth2UserInfoFactoryTest {


    @ParameterizedTest
    @MethodSource("provideValidOAuth2UserInfo")
    void 여러_PROVIDER를_통해_OAuth2UserInfo를_생성한다(String provider, Map<String, Object> attributes,
            Class<? extends OAuth2UserInfo> expectedClass,
            String expectedId, String expectedEmail, String expectedName, String expectedImagUrl) {
        // given
        // when
        OAuth2UserInfo result = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, attributes);

        // then
        assertThat(result).isInstanceOf(expectedClass);
        assertThat(result.getId()).isEqualTo(expectedId);
        assertThat(result.getEmail()).isEqualTo(expectedEmail);
        assertThat(result.getName()).isEqualTo(expectedName);
        assertThat(result.getImageUrl()).isEqualTo(expectedImagUrl);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "naver", "facebook"})
    void 지원하지_않는_PROVIDER인_경우_예외가_발생한다(String unsupportedProvider) {
        // given
        Map<String, Object> attributes = Map.of("id", "12345");

        // when & then
        assertThatThrownBy(() ->
                OAuth2UserInfoFactory.getOAuth2UserInfo(unsupportedProvider, attributes)
        ).isInstanceOf(UnsupportedProviderTypeException.class);
    }

    @Test
    @DisplayName("null_제공자인_경우_예외가_발생한다")
    void null_제공자인_경우_예외가_발생한다() {
        // given
        Map<String, Object> attributes = Map.of("id", "12345");

        // when & then
        assertThatThrownBy(() ->
                OAuth2UserInfoFactory.getOAuth2UserInfo(null, attributes)
        ).isInstanceOf(NullPointerException.class);
    }

    static Stream<Arguments> provideValidOAuth2UserInfo() {
        return Stream.of(
                Arguments.of(
                        "google",
                        Map.of("sub", "12345",
                                "email", "google@gmail.com",
                                "name", "구글 사용자",
                                "picture", "image/url"),
                        GoogleOAuth2UserInfo.class,
                        "12345", "google@gmail.com", "구글 사용자", "image/url"
                ),
                Arguments.of(
                        "kakao",
                        Map.of("id", 123456789L,
                                "kakao_account", Map.of(
                                        "email", "kakao@gmail.com",
                                        "profile", Map.of("profile_image_url", "image/url")),
                                "properties", Map.of("nickname", "카카오사용자")
                        ),
                        KakaoOAuth2UserInfo.class,
                        "123456789", "kakao@gmail.com", "카카오사용자", "image/url"
                ),
                Arguments.of(
                        "GOOGLE",
                        Map.of("sub", "12345",
                                "email", "google@gmail.com",
                                "name", "구글 사용자",
                                "picture", "image/url"),
                        GoogleOAuth2UserInfo.class,
                        "12345", "google@gmail.com", "구글 사용자", "image/url"
                ),
                Arguments.of(
                        "Kakao",
                        Map.of("id", 123456789L,
                                "kakao_account", Map.of(
                                        "email", "kakao@gmail.com",
                                        "profile", Map.of("profile_image_url", "image/url")),
                                "properties", Map.of("nickname", "카카오사용자")
                        ),
                        KakaoOAuth2UserInfo.class,
                        "123456789", "kakao@gmail.com", "카카오사용자", "image/url"
                )
        );
    }
}