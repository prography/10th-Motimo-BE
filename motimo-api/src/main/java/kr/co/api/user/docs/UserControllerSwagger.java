package kr.co.api.user.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import kr.co.api.exception.ErrorResponse;
import kr.co.api.user.rqrs.UserIdRs;
import kr.co.api.user.rqrs.UserInterestsRq;
import kr.co.api.user.rqrs.UserRs;
import kr.co.api.user.rqrs.UserUpdateRq;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "사용자 API", description = "사용자 관련 API 목록입니다")
public interface UserControllerSwagger {

    @Operation(summary = "내 정보 조회", description = "로그인한 사용자의 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 정보 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content)
    })
    UserRs getMyProfile(UUID userId);

    @Operation(summary = "사용자 프로필 조회", description = "userId 로 특정 사용자의 프로필을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserRs.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    UserRs getProfile(@Parameter(description = "조회할 사용자 ID", example = "0199eb6a‑…") UUID userId);


    @Operation(summary = "나의 관심사 수정", description = "사용자가 보유한 관심사를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관심사 수정 성공", content = @Content(schema = @Schema(implementation = UserIdRs.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content)
    })
    UserIdRs updateMyInterests(
            UUID userId,
            @Schema(implementation = UserInterestsRq.class) @RequestBody UserInterestsRq request
    );

    @Operation(summary = "프로필 수정",
            description = "닉네임,소개글,관심사 및 프로필 이미지를 수정합니다. " +
                    "유저 이름이 null이거나 빈 문자열(공백만 있는 경우 포함)이면 기존 이름을 사용합니다. " +
                    "이미지 업로드가 없으면 기존 이미지를 유지합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = UserIdRs.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청/파일 형식 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content)
    })
    UserIdRs updateMyProfile(UUID userId,
            @RequestPart @Schema(implementation = UserUpdateRq.class) UserUpdateRq request,
            @Parameter(description = "프로필 이미지 파일", content = @Content(mediaType = "multipart/form-data"))
            @RequestPart(name = "file", required = false) MultipartFile image);

    @Operation(summary = "유저 탈퇴")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "탈퇴 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content)
    })
    void deleteUser(UUID userId);
}
