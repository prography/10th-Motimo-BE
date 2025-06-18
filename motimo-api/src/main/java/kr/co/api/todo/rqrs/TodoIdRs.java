package kr.co.api.todo.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record TodoIdRs(
        @Schema(description = "투두 아이디")
        UUID todoId
) {

}
