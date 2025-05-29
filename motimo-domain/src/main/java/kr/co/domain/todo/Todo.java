package kr.co.domain.todo;

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
    private String title;
    private LocalDate date;
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
}

