package kr.co.infra.rdb.goal.entity;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import kr.co.domain.goal.DueDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class DueDateEmbeddable {
    private Integer month;
    private LocalDate dueDate;

    public static DueDateEmbeddable from(DueDate dueDate) {
        return new DueDateEmbeddable(dueDate.getMonth(), dueDate.getDate());
    }

    public DueDate toDomain() {
        return new DueDate(month, dueDate);
    }
}