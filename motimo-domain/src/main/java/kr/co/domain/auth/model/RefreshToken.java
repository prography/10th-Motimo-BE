package kr.co.domain.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class RefreshToken {
    @Builder.Default
    private UUID id = null;
    private UUID tokenId;
    private UUID userId;
    private String token;
}
