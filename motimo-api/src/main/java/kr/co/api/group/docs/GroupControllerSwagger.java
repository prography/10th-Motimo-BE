package kr.co.api.group.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import kr.co.api.group.rqrs.GroupIdRs;
import kr.co.api.group.rqrs.GroupJoinRq;
import kr.co.api.group.rqrs.GroupMemberRs;
import kr.co.api.group.rqrs.GroupMessageIdRs;
import kr.co.api.group.rqrs.JoinedGroupRs;
import kr.co.api.group.rqrs.message.GroupChatRs;
import kr.co.api.group.rqrs.message.NewMessageRs;
import kr.co.domain.common.pagination.PagingDirection;
import kr.co.domain.group.reaction.ReactionType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "그룹 API", description = "그룹 관련 API 목록입니다")
public interface GroupControllerSwagger {

    @Operation(summary = "참여중인 그룹 목록 API", description = "참여중인 그룹을 조회합니다.")
    List<JoinedGroupRs> getJoinedGroups(UUID userId);

    @Operation(summary = "랜덤 그룹 가입 API", description = "랜덤으로 그룹에 가입합니다.")
    GroupIdRs joinRandomGroup(UUID userId, @RequestBody GroupJoinRq rq);

    @Operation(summary = "그룹내 새 메시지 조회 API", description = " 사용자가 마지막으로 읽은 커서 이후 새로 작성된 메시지 개수를 반환합니다.")
    NewMessageRs getNewGroupMessages(
            UUID userId,
            @Parameter(description = "그룹 ID") UUID groupId,
            @Parameter(description = "마지막으로 읽은 커서", required = false) String latestCursor);

    @Operation(summary = "그룹 채팅 조회 API", description = "커서,방향,limit 을 이용해 이전, 이후의 메시지 데이터를 가져옵니다.")
    GroupChatRs getGroupChat(
            UUID userId,
            @Parameter(description = "그룹 ID") UUID groupId,
            @Parameter(description = "한 번에 가져올 메시지 수", schema = @Schema(defaultValue = "10", minimum = "1", maximum = "100")) int limit,
            @Parameter(description = "커서 값", required = false) String cursor,
            @Parameter(description = "페이징 방향", schema = @Schema(implementation = PagingDirection.class)) PagingDirection direction);

    @Operation(summary = "그룹 리액션 API", description = "그룹 리액션을 생성합니다.")
    GroupMessageIdRs createGroupReaction(UUID userId, @PathVariable UUID messageId,
            @RequestParam ReactionType reactionType);

    @Operation(summary = "그룹 멤버 조회 API", description = "그룹 멤버 목록을 조회합니다.")
    List<GroupMemberRs> getGroupMembers(UUID userId, @PathVariable UUID groupId);

    @Operation(summary = "그룹 나가기 API", description = "그룹에서 나가게 됩니다.")
    void exitGroup(UUID userId, @PathVariable UUID groupId);

    @Operation(summary = "찌르기 API", description = "특정 사용자에게 찌르기 알람을 보냅니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "알림 보내기 성공"),
            @ApiResponse(responseCode = "400", description = "찌르기 알림 횟수 초과")
    })
    void sendPokeNotification(UUID userId, @PathVariable UUID groupId, @PathVariable UUID targetUserId);
}
