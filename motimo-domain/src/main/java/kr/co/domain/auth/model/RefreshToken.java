package kr.co.domain.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class RefreshToken {
    @Builder.Default
    private UUID id = null;
    @NonNull
    private UUID tokenId;
    @NonNull
    private UUID userId;
    @NonNull
    private String token;
}
