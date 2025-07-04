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
import kr.co.api.group.rqrs.message.TodoMessageContentRs;
import kr.co.api.security.annotation.AuthUser;
import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.group.MessageType;
import kr.co.domain.group.reaction.ReactionType;
import kr.co.domain.todo.Emotion;
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
    public CustomSlice<GroupMessageItemRs> getGroupChat(@PathVariable UUID groupId,
            @RequestParam int page, @RequestParam int size) {
        List<GroupMessageItemRs> groupChatItems = List.of(
                new GroupMessageItemRs(
                        MessageType.ENTER,
                        UUID.randomUUID(),
                        "김하얀",
                        null,
                        4,
                        false
                ),
                new GroupMessageItemRs(
                        MessageType.TODO,
                        UUID.randomUUID(),
                        "김안검",
                        new TodoMessageContentRs(
                                UUID.randomUUID(),
                                "완료한 투두 제목입니다",
                                Emotion.PROUD,
                                "완료한 투두 내용입니다",
                                "file url입니다."
                        ),
                        4,
                        true
                ),
                new GroupMessageItemRs(
                        MessageType.TODO,
                        UUID.randomUUID(),
                        "김안검",
                        new TodoMessageContentRs(
                                UUID.randomUUID(),
                                "완료한 투두 제목입니다",
                                Emotion.GROWN,
                                null,
                                null
                        ),
                        0,
                        false
                )
        );
        return new CustomSlice<>(groupChatItems, true);
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
