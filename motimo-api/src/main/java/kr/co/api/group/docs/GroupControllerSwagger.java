package kr.co.api.group.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import kr.co.api.group.rqrs.GroupChatItemRs;
import kr.co.api.group.rqrs.GroupMemberRs;
import kr.co.api.group.rqrs.JoinedGroupRs;
import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.reaction.ReactionType;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "그룹 API", description = "그룹 관련 API 목록입니다")
public interface GroupControllerSwagger {
    @Operation(summary = "참여중인 그룹 목록 API", description = "참여중인 그룹을 조회합니다.")
    List<JoinedGroupRs> getJoinedGroup(UUID userId);

    @Operation(summary = "랜덤 그룹 가입 API", description = "랜덤으로 그룹에 가입합니다.")
    UUID joinRandomGroup(UUID userId);

    @Operation(summary = "그룹 채팅 조회 API", description = "그룹 채팅을 조회합니다.")
    CustomSlice<GroupChatItemRs> getGroupChat(UUID userId, @RequestParam int page, @RequestParam int size);

    @Operation(summary = "그룹 리액션 API", description = "그룹 리액션을 생성합니다.")
    UUID createGroupReaction(UUID userId, UUID messageId, ReactionType reactionType);

    @Operation(summary = "그룹 멤버 조회 API", description = "그룹 멤버 목록을 조회합니다.")
    List<GroupMemberRs> getGroupMembers(UUID userId, UUID groupId);

    @Operation(summary = "그룹 나가기 API", description = "그룹에서 나가게 됩니다.")
    void exitsGroup(UUID groupId);
}
