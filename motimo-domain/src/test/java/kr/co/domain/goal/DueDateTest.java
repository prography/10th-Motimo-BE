package kr.co.domain.goal;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DueDate 도메인 테스트")
public class DueDateTest {
    @Test
    void 개월수로_생성시_마감_날짜가_계산된다() {
        final int month = 4;

        DueDate dueDate = new DueDate(month);
        LocalDate calculatedDueDate = LocalDate.now().plusMonths(month);

        assertThat(dueDate.getMonth()).isEqualTo(month);
        assertThat(dueDate.getDueDate()).isEqualTo(calculatedDueDate);
    }
}
