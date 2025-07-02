package kr.co.api.poke.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "찌르기 API", description = "찌르기 관련 API 목록입니다")
public interface PokeControllerSwagger {
    @Operation(summary = "찌르기 API", description = "특정 사용자에게 찌르기 알람을 보냅니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "알림 보내기 성공"),
            @ApiResponse(responseCode = "400", description = "찌르기 알림 횟수 초과")
    })
    void sendPokeNotification(UUID userId, @PathVariable UUID targetUserId);
}
