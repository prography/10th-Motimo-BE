package kr.co.api.subgoal.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import kr.co.api.exception.ErrorResponse;
import kr.co.api.subgoal.rqrs.SubGoalIdRs;
import kr.co.api.subgoal.rqrs.TodoCreateRq;
import kr.co.api.todo.rqrs.TodoIdRs;
import kr.co.api.todo.rqrs.TodoRs;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "세부 목표 API", description = "세부 목표 관련 API 목록입니다")
public interface SubGoalControllerSwagger {

    @Operation(summary = "세부 목표 완료/완료 취소 API", description = "세부 목표를 완료/완료 취소 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "완료/취소 처리 성공"),
            @ApiResponse(responseCode = "403", description = "완료 처리 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "세부 목표를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    SubGoalIdRs subGoalCompleteToggle(UUID userId, @PathVariable UUID subGoalId);

    @Operation(summary = "TODO 생성", description = "세부 목표에 새로운 TODO를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "TODO 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content)
    })
    TodoIdRs createTodo(
            UUID userId,
            @Parameter(description = "세부 목표 ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID subGoalId,
            @RequestBody @Schema(implementation = TodoCreateRq.class) TodoCreateRq request
    );

    @Operation(summary = "세부 목표별 TODO 목록 조회", description = "특정 세부 목표의 TODO 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "TODO 목록 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TodoRs.class)))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 데이터",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    List<TodoRs> getIncompleteOrTodayTodos(
            @Parameter(description = "세부 목표 ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID subGoalId
    );
}
