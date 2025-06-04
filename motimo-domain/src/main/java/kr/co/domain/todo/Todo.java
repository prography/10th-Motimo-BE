package kr.co.domain.todo;

import kr.co.domain.common.exception.AccessDeniedException;
import kr.co.domain.todo.exception.TodoErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class Todo {

    @Builder.Default
    private UUID id = null;
    private UUID subGoalId;
    private UUID authorId;
    private String title;
    private LocalDate date;
    @Builder.Default
    private boolean completed = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void update(String newTitle, LocalDate newDate) {
        this.title = newTitle;
        this.date = newDate;
    }

    public void toggleCompletion() {
        this.completed = !this.completed;
    }

    public void validateAuthor(UUID userId) {
        if (!this.authorId.equals(userId)) {
            throw new AccessDeniedException(TodoErrorCode.TODO_ACCESS_DENIED);
        }
    }
}

