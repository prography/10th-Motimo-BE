package kr.co.domain.goal;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DueDate {
    private Integer month;
    private LocalDate date;

    public static DueDate of(int month) {
        LocalDate currentDate = LocalDate.now();
        return new DueDate(month, currentDate.plusMonths(month));
    }

    public static DueDate of(LocalDate localDate) {
        return new DueDate(null, localDate);
    }

    public boolean isMonth() {
        return month != null;
    }
}
