package kr.co.api.group;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.co.api.group.docs.GroupControllerSwagger;
import kr.co.api.group.rqrs.GroupIdRs;
import kr.co.api.group.rqrs.GroupJoinRq;
import kr.co.api.group.rqrs.GroupMemberRs;
import kr.co.api.group.rqrs.GroupMessageIdRs;
import kr.co.api.group.rqrs.JoinedGroupRs;
import kr.co.api.group.rqrs.message.GroupChatRs;
import kr.co.api.group.rqrs.message.NewMessageRs;
import kr.co.api.group.service.GroupCommandService;
import kr.co.api.group.service.GroupMessageQueryService;
import kr.co.api.group.service.GroupQueryService;
import kr.co.api.security.annotation.AuthUser;
import kr.co.domain.common.pagination.PagingDirection;
import kr.co.domain.group.reaction.ReactionType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/groups")
public class GroupController implements GroupControllerSwagger {

    private final GroupMessageQueryService groupMessageQueryService;
    private final GroupCommandService groupCommandService;
    private final GroupQueryService groupQueryService;

    public GroupController(final GroupMessageQueryService groupMessageQueryService,
            final GroupCommandService groupCommandService,
            final GroupQueryService groupQueryService) {
        this.groupMessageQueryService = groupMessageQueryService;
        this.groupCommandService = groupCommandService;
        this.groupQueryService = groupQueryService;
    }

    @GetMapping("/me")
    public List<JoinedGroupRs> getJoinedGroups(@AuthUser UUID userId) {
        return groupQueryService.getJoinedGroupList(userId).stream().map(
                JoinedGroupRs::from
        ).toList();
    }

    @PostMapping("/random-join")
    public GroupIdRs joinRandomGroup(@AuthUser UUID userId, @RequestBody GroupJoinRq rq) {
        return new GroupIdRs(groupCommandService.joinGroup(userId, rq.goalId()));
    }

    @GetMapping("/{groupId}/new-chats")
    public NewMessageRs getNewGroupMessages(@AuthUser UUID userId, @PathVariable UUID groupId,
            @RequestParam(required = false) String latestCursor) {
        return NewMessageRs.from(
                groupMessageQueryService.findNewChats(userId, groupId, latestCursor));
    }

    @GetMapping("/{groupId}/chats")
    public GroupChatRs getGroupChat(
            @AuthUser UUID userId,
            @PathVariable UUID groupId,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "BEFORE") PagingDirection direction) {

        return GroupChatRs.from(
                groupMessageQueryService.findMessagesByGroupIdWithCursor(
                        userId, groupId, cursor, limit, direction));
    }

    @PostMapping("/message/{messageId}/reaction")
    public GroupMessageIdRs createGroupReaction(@AuthUser UUID userId, @PathVariable UUID messageId,
            @RequestParam ReactionType type) {
        return new GroupMessageIdRs(UUID.randomUUID());
    }

    @GetMapping("/{groupId}/members")
    public List<GroupMemberRs> getGroupMembers(@AuthUser UUID userId, @PathVariable UUID groupId) {
        return List.of(
                new GroupMemberRs(UUID.randomUUID(), "닉네임1", LocalDateTime.now(), true),
                new GroupMemberRs(UUID.randomUUID(), "닉네임1", LocalDateTime.now(), false),
                new GroupMemberRs(userId, "본인입니다", LocalDateTime.now(), false)
        );
    }

    @DeleteMapping("/{groupId}/members/me")
    public void exitGroup(@AuthUser UUID userId, @PathVariable UUID groupId) {
        groupCommandService.leaveGroup(userId, groupId);
    }

    @PostMapping("/{groupId}/members/{targetUserId}/poke")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendPokeNotification(@AuthUser UUID userId, @PathVariable UUID groupId,
            @PathVariable UUID targetUserId) {
        groupCommandService.createPokeNotification(userId, groupId, targetUserId);
    }
}
