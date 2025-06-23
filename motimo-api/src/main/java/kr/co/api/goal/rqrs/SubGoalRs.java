package kr.co.api.goal.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;
import kr.co.api.todo.rqrs.TodoRs;

public record SubGoalRs(
        @Schema(description = "세부 목표 Id", example = "0197157f-aea4-77bb-8581-3213eb5bd2ae")
        UUID id,

        @Schema(description = "세부 목표 이름", example = "책 한 권 끝내기")
        String title,

        @Schema(description = "세부 목표에 해당하는 투두 목록")
        List<TodoRs> todos
) {

}
