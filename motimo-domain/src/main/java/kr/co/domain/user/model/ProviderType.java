package kr.co.domain.user.model;

import kr.co.domain.user.exception.UnsupportedProviderTypeException;

public enum ProviderType {
    LOCAL,
    GOOGLE,
    KAKAO
    ;

    public static ProviderType of(String provider) {
        try {
            return valueOf(provider.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedProviderTypeException();
        }
    }
}
