package kr.co.api.goal.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;
import kr.co.api.goal.dto.SubGoalDto;
import kr.co.api.todo.rqrs.TodoRs;

public record SubGoalRs(
        @Schema(description = "세부 목표 Id", example = "0197157f-aea4-77bb-8581-3213eb5bd2ae", requiredMode = Schema.RequiredMode.REQUIRED)
        UUID id,

        @Schema(description = "세부 목표 이름", example = "책 한 권 끝내기", requiredMode = Schema.RequiredMode.REQUIRED)
        String title,

        boolean isCompleted,

        @Schema(description = "세부 목표에 해당하는 투두 목록")
        List<TodoRs> todos
) {

        public static SubGoalRs from(SubGoalDto dto) {
                return new SubGoalRs(
                        dto.id(),
                        dto.title(),
                        dto.isCompleted(),
                        dto.todos().stream().map(TodoRs::from).toList()
                );
        }
}
