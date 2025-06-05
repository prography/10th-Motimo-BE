package kr.co.domain.goal;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class DueDate {
    private Integer month;
    private LocalDate dueDate;
}
