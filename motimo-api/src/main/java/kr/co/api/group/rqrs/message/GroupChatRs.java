package kr.co.api.group.rqrs.message;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import kr.co.api.group.rqrs.GroupMessageItemRs;
import kr.co.api.group.service.dto.GroupChatDto;

@Schema(description = "그룹 메시지 응답")
public record GroupChatRs(
        @Schema(description = "메시지 목록")
        List<GroupMessageItemRs> messages,
        @Schema(description = "이전 메시지 요청용 커서")
        String prevCursor,
        @Schema(description = "다음 메시지 요청용 커서")
        String nextCursor,
        @Schema(description = "이전 메시지 존재 여부")
        boolean hasBefore,
        @Schema(description = "다음 메시지 존재 여부")
        boolean hasAfter
) {

    public static GroupChatRs from(GroupChatDto dto) {
        return new GroupChatRs(
                dto.messageItems().stream().map(GroupMessageItemRs::from).toList(),
                dto.prevCursor(),
                dto.nextCursor(),
                dto.hasBefore(),
                dto.hasAfter()
        );
    }
}