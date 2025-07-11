package kr.co.api.group.service.dto;

import java.util.List;

public record GroupChatDto(
        List<GroupMessageItemDto> messageItems,
        String prevCursor,        // 이전 페이지 커서 (더 오래된 메시지)
        String nextCursor,         // 다음 페이지 커서  (더 최신 메시지)
        boolean hasBefore,          // 이전 메시지 존재 여부
        boolean hasAfter          // 이후 메시지 존재 여부
) {

}
