package kr.co.domain.goal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import kr.co.domain.subGoal.SubGoal;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class Goal{
    private UUID id;
    private String title;
    private DueDate dueDate;
    private List<SubGoal> subGoals = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
