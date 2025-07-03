package kr.co.domain.group;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Group {
    @Builder.Default()
    private UUID id = null;
//    private List<GroupMember> members = new ArrayList<>();
    private LocalDateTime finishedDate;

    @Builder(builderMethodName = "createGroup")
    private Group() {
    }
}
