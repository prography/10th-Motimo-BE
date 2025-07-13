package kr.co.api.group.service.dto;

import java.util.UUID;

public record GroupDto(
        UUID groupId,
        String name
) {

}
