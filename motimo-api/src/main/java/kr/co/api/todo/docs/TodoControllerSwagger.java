package kr.co.api.todo.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import kr.co.api.todo.rqrs.TodoResultRq;
import kr.co.api.todo.rqrs.TodoResultRs;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "투두 API", description = "투두 관련 API 목록입니다")
public interface TodoControllerSwagger {

    @Operation(summary = "투두 결과(기록) 제출하기", description = "투두 수행 결과를 제출합니다. 파일을 첨부할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "TODO 결과 제출 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "TODO를 찾을 수 없음")
    })
    void submitResult(
            @Parameter(description = "USER ID", required = true) UUID userId,
            @Parameter(description = "TODO ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @RequestPart @Schema(implementation = TodoResultRq.class) TodoResultRq request,
            @RequestPart(name = "file", required = false) MultipartFile file
    );

    @Operation(summary = "투두 결과(기록) 조회", description = "특정 투두의 결과(기록)를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TODO 결과 조회 성공", content = @Content(schema = @Schema(implementation = TodoResultRs.class))),
            @ApiResponse(responseCode = "204", description = "결과 없음"),
            @ApiResponse(responseCode = "404", description = "TODO를 찾을 수 없음")
    })
    ResponseEntity<TodoResultRs> getTodoResult(
            @Parameter(description = "TODO ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID todoId
    );

    @Operation(summary = "투두 완료 상태 변경", description = "투두 완료 상태를 토글합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "TODO 완료 상태 변경 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "TODO를 찾을 수 없음")
    })
    void cancelResult(
            @Parameter(description = "USER ID", required = true) UUID userId,
            @Parameter(description = "TODO ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id
    );

    @Operation(summary = "투두 삭제", description = "특정 투두를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "TODO 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "TODO를 찾을 수 없음")
    })
    void deleteById(
            @Parameter UUID userId,
            @Parameter(description = "TODO ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id
    );
}