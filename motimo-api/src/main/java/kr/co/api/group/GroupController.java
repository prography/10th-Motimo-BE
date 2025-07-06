package kr.co.api.group;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.co.api.group.docs.GroupControllerSwagger;
import kr.co.api.group.rqrs.GroupIdRs;
import kr.co.api.group.rqrs.GroupJoinRq;
import kr.co.api.group.rqrs.GroupMemberRs;
import kr.co.api.group.rqrs.GroupMessageIdRs;
import kr.co.api.group.rqrs.GroupMessageItemRs;
import kr.co.api.group.rqrs.JoinedGroupRs;
import kr.co.api.group.service.GroupMessageQueryService;
import kr.co.api.security.annotation.AuthUser;
import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.group.reaction.ReactionType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/groups")
public class GroupController implements GroupControllerSwagger {

    private final GroupMessageQueryService groupMessageQueryService;

    public GroupController(GroupMessageQueryService groupMessageQueryService) {
        this.groupMessageQueryService = groupMessageQueryService;
    }

    @GetMapping("/me")
    public List<JoinedGroupRs> getJoinedGroup(@AuthUser UUID userId) {
        return List.of(
                new JoinedGroupRs("백다방 백잔 먹기", LocalDateTime.now(), false),
                new JoinedGroupRs("충전기 만들기", LocalDateTime.now(), true)
        );
    }

    @PostMapping("/random-join")
    public GroupIdRs joinRandomGroup(@AuthUser UUID userId, @RequestBody GroupJoinRq rq) {
        return new GroupIdRs(UUID.randomUUID());
    }

    @GetMapping("/{groupId}/chats")
    public CustomSlice<GroupMessageItemRs> getGroupChat(@AuthUser UUID userId,
            @PathVariable UUID groupId, @RequestParam int page, @RequestParam int size) {

        return groupMessageQueryService.findAllByGroupIdWithPaging(userId, groupId, page, size)
                .map(GroupMessageItemRs::from);
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
    public void exitGroup(@PathVariable UUID groupId) {

    }
}
