package kr.co.api.todo.docs;

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
import kr.co.api.todo.rqrs.TodoIdRs;
import kr.co.api.todo.rqrs.TodoResultIdRs;
import kr.co.api.todo.rqrs.TodoResultRq;
import kr.co.api.todo.rqrs.TodoResultRs;
import kr.co.api.todo.rqrs.TodoRs;
import kr.co.api.todo.rqrs.TodoUpdateRq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "투두 API", description = "투두 관련 API 목록입니다")
public interface TodoControllerSwagger {

    @Operation(summary = "투두 결과(기록) 제출하기", description = "투두 수행 결과를 제출합니다. 파일을 첨부할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TODO 결과 제출 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "404", description = "TODO를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    TodoResultIdRs upsertTodoResult(
            UUID userId,
            @Parameter(description = "TODO ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID todoId,
            @RequestPart @Schema(implementation = TodoResultRq.class) TodoResultRq request,
            @RequestPart(name = "file", required = false) MultipartFile file
    );

    @Operation(summary = "나의 TODO 목록 조회", description = "로그인한 사용자의 TODO 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TODO 목록 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TodoRs.class)))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content)
    })
    List<TodoRs> getMyTodos(UUID userId);

    @Operation(summary = "투두 결과(기록) 조회", description = "특정 투두의 결과(기록)를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TODO 결과 조회 성공", content = @Content(schema = @Schema(implementation = TodoResultRs.class))),
            @ApiResponse(responseCode = "204", description = "결과 없음", content = @Content),
            @ApiResponse(responseCode = "404", description = "TODO를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<TodoResultRs> getTodoResult(
            @Parameter(description = "TODO ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID todoId
    );

    @Operation(summary = "투두 완료 상태 변경", description = "투두 완료 상태를 토글합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "TODO 완료 상태 변경 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "403", description = "투두 수정에 대한 권한이 없는 사용자", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "TODO를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    TodoIdRs toggleTodoCompletion(
            UUID userId,
            @Parameter(description = "TODO ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID todoId
    );

    @Operation(summary = "투두 업데이트", description = "투두 내용을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "투두 내용 수정 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "403", description = "투두 수정에 대한 권한이 없는 사용자", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "투두를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    TodoIdRs updateTodo(
            UUID userId,
            @Parameter(description = "TODO ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID todoId,
            @RequestBody @Schema(implementation = TodoUpdateRq.class) TodoUpdateRq request
    );

    @Operation(summary = "투두 삭제", description = "특정 투두를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "TODO 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "403", description = "투두 삭제에 대한 권한이 없는 사용자", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "TODO를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void deleteById(
            UUID userId,
            @Parameter(description = "TODO ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID todoId
    );

    @Operation(summary = "투두 결과 삭제", description = "특정 투두 결과를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "TODO 결과 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "403", description = "투두 결과 삭제에 대한 권한이 없는 사용자", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "TODO 결과를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void deleteTodoResultById(
            UUID userId,
            @Parameter(description = "TODO ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID todoId
    );
}