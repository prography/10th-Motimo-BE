package kr.co.domain.group;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import kr.co.domain.group.exception.UserNotInGroupException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Group {

    public static final int MAX_GROUP_MEMBER_COUNT = 6;

    @Builder.Default()
    private UUID id = null;

    @Builder.Default()
    private String name = null;

    @Builder.Default()
    private List<GroupMember> members = new ArrayList<>();

    private LocalDateTime finishedDate;

    @Builder(builderMethodName = "createGroup")
    private Group() {
    }


    public GroupMember getMember(UUID memberId) {
        return members.stream().filter(member -> member.getMemberId().equals(memberId)).findFirst().orElseThrow(UserNotInGroupException::new);
    }
}
