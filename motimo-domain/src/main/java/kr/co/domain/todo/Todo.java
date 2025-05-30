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
    @Builder.Default
    private LocalDate date = LocalDate.now();
    @Builder.Default
    private boolean completed = false;
    private TodoResult result;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void update(String newTitle, LocalDate newDate) {
        this.title = newTitle;
        this.date = newDate;
    }

    public void complete(TodoResult todoResult) {
        this.completed = true;
        this.result = todoResult;
    }

    public void cancelCompletion() {
        this.completed = false;
    }

    public void validateAuthor(UUID userId) {
        if (!this.authorId.equals(userId)) {
            throw new AccessDeniedException(TodoErrorCode.TODO_ACCESS_DENIED);
        }
    }
}

