package kr.co.api.todo.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.api.todo.rqrs.TodoCreateRq;
import kr.co.api.todo.rqrs.TodoResultRq;
import kr.co.api.todo.rqrs.TodoRs;
import kr.co.domain.common.pagination.CustomSlice;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "TODO API", description = "TODO 관련 API 목록입니다")
public interface TodoControllerSwagger {
    @Operation(summary = "TODO 생성", description = "새로운 TODO를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "TODO 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    void createTodo(
            @Parameter(hidden = true) UUID userId,
            @RequestBody @Valid TodoCreateRq request
    );

    @Operation(summary = "TODO 결과 제출", description = "완료된 TODO의 결과를 제출합니다. 파일 첨부가 가능합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "결과 제출 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "TODO를 찾을 수 없음")
    })
    void summitResult(
            @Parameter(hidden = true) UUID userId,
            @Parameter(description = "TODO ID", required = true) @PathVariable UUID id,
            @RequestBody @Valid TodoResultRq request,
            @Parameter(description = "첨부 파일 (선택사항)") @RequestPart(required = false) MultipartFile file
    );

    @Operation(
            summary = "TODO 상세 조회",
            description = "특정 TODO의 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = TodoRs.class))
            ),
            @ApiResponse(responseCode = "404", description = "TODO를 찾을 수 없음")
    })
    TodoRs getTodoDetail(
            @Parameter(description = "TODO ID", required = true) @PathVariable UUID id
    );

    @Operation(summary = "세부 목표별 TODO 목록 조회", description = "특정 세부 목표에 속한 TODO 목록을 페이징으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = CustomSlice.class))
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 페이징 파라미터")
    })
    CustomSlice<TodoRs> getTodosBySubGoal(
            @Parameter(description = "서브 목표 ID", required = true) @PathVariable UUID subGoalId,
            @Parameter(description = "페이지 번호 (0부터 시작)", required = true) @RequestParam("page") int page,
            @Parameter(description = "페이지 크기", required = true) @RequestParam("size") int size
    );

    @Operation(summary = "내 TODO 목록 조회", description = "현재 로그인한 사용자의 TODO 목록을 페이징으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = CustomSlice.class))
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 페이징 파라미터"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    CustomSlice<TodoRs> getMyTodos(
            @Parameter(hidden = true) UUID userId,
            @Parameter(description = "페이지 번호 (0부터 시작)", required = true) @RequestParam("page") int page,
            @Parameter(description = "페이지 크기", required = true) @RequestParam("size") int size
    );

    @Operation(summary = "TODO 결과 취소", description = "제출된 TODO 결과를 취소하고 미완료 상태로 되돌립니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "결과 취소 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "TODO를 찾을 수 없음")
    })
    void cancelResult(
            @Parameter(hidden = true) UUID userId,
            @Parameter(description = "TODO ID", required = true) @PathVariable UUID id
    );

    @Operation(summary = "TODO 삭제", description = "특정 TODO를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "TODO를 찾을 수 없음")
    })
    void deleteById(
            @Parameter(hidden = true) UUID userId,
            @Parameter(description = "TODO ID", required = true) @PathVariable UUID id
    );
}
