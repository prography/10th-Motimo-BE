package kr.co.domain.todo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.domain.common.exception.AccessDeniedException;
import kr.co.domain.todo.exception.TodoErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Todo {

    @Builder.Default
    private UUID id = null;
    private UUID subGoalId;
    private UUID userId;
    private String title;
    private LocalDate date;
    @Builder.Default
    private TodoStatus status = TodoStatus.INCOMPLETE;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void update(String newTitle, LocalDate newDate) {
        this.title = newTitle;
        this.date = newDate;
    }

    public void toggleCompletion() {
        this.status = (this.status == TodoStatus.INCOMPLETE)
                ? TodoStatus.COMPLETE
                : TodoStatus.INCOMPLETE;
    }

    public void validateOwner(UUID userId) {
        if (!this.userId.equals(userId)) {
            throw new AccessDeniedException(TodoErrorCode.TODO_ACCESS_DENIED);
        }
    }
    
    public boolean isComplete() {
        return this.status == TodoStatus.COMPLETE;
    }

    @Builder(builderMethodName = "createTodo")
    private Todo(UUID subGoalId, UUID userId, String title, LocalDate date) {
        this.subGoalId = subGoalId;
        this.userId = userId;
        this.title = title;
        this.date = date;
        this.status = TodoStatus.INCOMPLETE;
    }
}

