package kr.co.api.group.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.api.group.rqrs.message.GroupMessageContentRs;
import kr.co.api.group.service.dto.GroupMessageItemDto;

public record GroupMessageItemRs(
        UUID messageId,
        @Schema(description = "보낸 사용자 아이디")
        UUID userId,
        @Schema(description = "보낸 사용자 닉네임")
        String userName,
        @Schema(description = "메세지 내용")
        GroupMessageContentRs message,
        @Schema(description = "리액션 갯수")
        int reactionCount,
        @Schema(description = "로그인한 사용자 리액션 여부")
        boolean hasUserReacted,
        @Schema(description = "메세지를 보낸 시간", type = "date")
        LocalDateTime sendAt
) {

    public static GroupMessageItemRs from(GroupMessageItemDto dto) {
        return new GroupMessageItemRs(
                dto.messageId(),
                dto.userId(),
                dto.userName(),
                new GroupMessageContentRs(dto.content()),
                dto.reactionCount(),
                dto.hasUserReacted(),
                dto.sendAt());
    }
}
