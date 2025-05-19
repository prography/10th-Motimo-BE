package kr.co.domain.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class User {

    @Builder.Default
    private final Long id = null;
    private final String email;
    private final String nickname;
    private final String password;

}
