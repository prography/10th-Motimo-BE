package kr.co.api.poke.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;

@Tag(name = "찌르기 API", description = "찌르기 관련 API 목록입니다")
public interface PokeControllerSwagger {
    @Operation(summary = "찌르기 API", description = "특정 사용자에게 찌르기 알람을 보냅니다.")
    void sendPokeNotification(UUID userId, UUID targetId);
}
