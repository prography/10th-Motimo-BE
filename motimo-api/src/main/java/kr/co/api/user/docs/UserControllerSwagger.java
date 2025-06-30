package kr.co.api.user.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import kr.co.api.exception.ErrorResponse;
import kr.co.api.todo.rqrs.TodoRs;

@Tag(name = "사용자 API", description = "사용자 관련 API 목록입니다")
public interface UserControllerSwagger {

    @Operation(summary = "나의 TODO 목록 조회", description = "로그인한 사용자의 TODO 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TODO 목록 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TodoRs.class)))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content)
    })
    List<TodoRs> getMyTodos(UUID userId);

}
