package kr.co.api.subgoal.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import kr.co.api.exception.ErrorResponse;
import kr.co.api.subgoal.rqrs.SubGoalIdRs;
import kr.co.api.subgoal.rqrs.TodoCreateRq;
import kr.co.api.todo.rqrs.TodoIdRs;
import kr.co.api.todo.rqrs.TodoRs;
import kr.co.domain.common.pagination.CustomSlice;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Operation(
            summary = "세부 목표의 모든 TODO 목록(슬라이스) 조회",
            description = """
                    특정 Sub‑Goal ID에 속한 모든 TODO를 슬라이스 방식(offset, size)으로
                    조회합니다.`hasNext` 플래그를 통해 다음 페이지 존재 여부를 판단합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "세부목표의 투두 리스트(완료, 미완료 모두)조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "세부 목표를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CustomSlice<TodoRs> getTodosBySubGoalIdWithSlice(
            @Parameter(description = "세부 목표 ID", required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID subGoalId,

            @Parameter(description = "시작 오프셋(0‑base)", example = "0")
            @RequestParam(defaultValue = "0") int offset,

            @Parameter(description = "한 페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") int size);

    @Operation(summary = "세부 목표별 미완료 또는 오늘 날짜 TODO 목록(슬라이스) 조회",
            description = """
                    Sub‑GoalID 기준으로 상태가 미완료이거나 
                    `date = 오늘` 인 TODO만 필터링하여 슬라이스로 반환합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "세부 목표의 오늘이거나 완료되지 않은 TODO 목록 조회 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 데이터",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "세부 목표를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CustomSlice<TodoRs> getIncompleteOrTodayTodosWithSlice(
            @Parameter(description = "세부 목표 ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID subGoalId,
            @Parameter(description = "시작 오프셋(0‑base)", example = "0")
            @RequestParam(defaultValue = "0") int offset,

            @Parameter(description = "한 페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") int size);
}
