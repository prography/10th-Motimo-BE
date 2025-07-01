package kr.co.api.group;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.co.api.group.docs.GroupControllerSwagger;
import kr.co.api.group.rqrs.GroupIdRs;
import kr.co.api.group.rqrs.GroupMemberRs;
import kr.co.api.group.rqrs.GroupMessageId;
import kr.co.api.group.rqrs.GroupMessageItemRs;
import kr.co.api.group.rqrs.JoinedGroupRs;
import kr.co.api.group.rqrs.message.TodoMessageContentRs;
import kr.co.api.security.annotation.AuthUser;
import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.group.MessageType;
import kr.co.domain.reaction.ReactionType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/groups")
public class GroupController implements GroupControllerSwagger {

    @GetMapping("/me")
    public List<JoinedGroupRs> getJoinedGroup(@AuthUser UUID userId) {
        return List.of(
                new JoinedGroupRs("백다방 백잔 먹기", LocalDateTime.now(), false),
                new JoinedGroupRs("충전기 만들기", LocalDateTime.now(), true)
        );
    }

    @PostMapping("/random-join")
    public GroupIdRs joinRandomGroup(@AuthUser UUID userId) {
        return new GroupIdRs(UUID.randomUUID());
    }

    // TODO
    @GetMapping("/{groupId}/chats")
    public CustomSlice<GroupMessageItemRs> getGroupChat(@PathVariable UUID groupId, @RequestParam int page, @RequestParam int size) {
        List<GroupMessageItemRs> groupChatItems = List.of(
                new GroupMessageItemRs(
                        MessageType.ENTER,
                        UUID.randomUUID(),
                        "김하얀",
                        null,
                        4
                ),
                new GroupMessageItemRs(
                        MessageType.TODO,
                        UUID.randomUUID(),
                        "김안검",
                        new TodoMessageContentRs(
                                UUID.randomUUID(),
                                "완료한 투두 제목입니다",
                                "완료한 투두 내용입니다",
                                "file url입니다."
                        ),
                        4
                ),
                new GroupMessageItemRs(
                        MessageType.TODO,
                        UUID.randomUUID(),
                        "김안검",
                        new TodoMessageContentRs(
                                UUID.randomUUID(),
                                "완료한 투두 제목입니다",
                                null,
                                null
                        ),
                        0
                )
        );
        return new CustomSlice<>(groupChatItems, true);
    }

    @PostMapping("/message/{messageId}/reaction")
    public GroupMessageId createGroupReaction(@AuthUser UUID userId, @PathVariable UUID messageId, @RequestParam ReactionType type) {
        return new GroupMessageId(UUID.randomUUID());
    }

    @GetMapping("/{groupId}/members")
    public List<GroupMemberRs> getGroupMembers(@AuthUser UUID userId, UUID groupId) {
        return List.of(
                new GroupMemberRs(UUID.randomUUID(), "닉네임1", LocalDateTime.now(), true),
                new GroupMemberRs(UUID.randomUUID(), "닉네임1", LocalDateTime.now(), false),
                new GroupMemberRs(userId, "본인입니다", LocalDateTime.now(), false)
        );
    }

    @DeleteMapping("/{groupId}/exits")
    public void exitsGroup(@PathVariable UUID groupId) {

    }
}
