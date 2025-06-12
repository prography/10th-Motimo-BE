package kr.co.infra.rdb.todo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.todo.dto.TodoSummary;
import kr.co.domain.todo.repository.TodoRepository;
import kr.co.infra.rdb.config.QueryDslConfig;
import kr.co.infra.rdb.todo.entity.TodoEntity;
import kr.co.infra.rdb.todo.entity.TodoResultEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@Import({TodoRepositoryImpl.class, QueryDslConfig.class})
@EntityScan(basePackages = "kr.co.infra.rdb")
class TodoRepositoryImplTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoJpaRepository todoJpaRepository;

    @Autowired
    private EntityManager testEntityManager;

    private JPAQueryFactory jpaQueryFactory;

    private UUID userId;
    private UUID subGoalId;
    private LocalDate today;
    private LocalDate yesterday;
    private LocalDate tomorrow;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        subGoalId = UUID.randomUUID();

        today = LocalDate.now();
        yesterday = today.minusDays(1);
        tomorrow = today.plusDays(1);
        setupTestData();
    }

    @AfterEach
    void clearContext() {
        testEntityManager.clear();
    }

    private void setupTestData() {
        // 완료되지 않은 투두
        createTodo("오늘 미완료 투두", false, today, subGoalId, userId, false);
        createTodo("내일 미완료 투두", false, tomorrow, subGoalId, userId, false);
        createTodo("어제 미완료 투두", false, yesterday, subGoalId, userId, false);
        createTodo("미완료 투두 날짜없음", false, null, subGoalId, userId, false);

        // 오늘 완료된 투두 (우선순위: 1 - 결과 없음, 우선순위: 2 - 결과 있음)
        createTodo("오늘 완료 투두 결과없음", true, today, subGoalId, userId, false);
        createTodo("오늘 완료 투두 결과있음", true, today, subGoalId, userId, true);

        // 과거에 완료된 투두
        createTodo("과거 완료 투두", true, yesterday, subGoalId, userId, false);

        // 다른 세부목표의 투두
        UUID otherSubGoalId = UUID.randomUUID();
        createTodo("다른 세부목표 오늘 미완료투두", false, today, otherSubGoalId, userId, false);
        createTodo("다른 세부목표 오늘 완료투두", true, today, otherSubGoalId, userId, false);

        // 다른 유저의 투두
        UUID otherUserId = UUID.randomUUID();
        createTodo("다른 유저 투두", false, today, otherSubGoalId, otherUserId, false);
    }

    private void createTodo(String title, boolean completed, LocalDate date,
            UUID subGoalId, UUID userId, boolean hasResult) {
        TodoEntity todo = TodoEntity.builder()
                .title(title)
                .completed(completed)
                .date(date)
                .subGoalId(subGoalId)
                .authorId(userId)
                .createdAt(LocalDateTime.now())
                .build();

        testEntityManager.persist(todo);

        if (hasResult) {
            testEntityManager.flush();
            TodoResultEntity result = TodoResultEntity.builder()
                    .todoId(todo.getId())
                    .build();
            testEntityManager.persist(result);
        }
        testEntityManager.flush();
    }

    @Test
    void findAllBySubGoalId_조건에_맞는_값만_조회되고_정렬된다() {
        // when
        CustomSlice<TodoSummary> result = todoRepository.findAllBySubGoalId(subGoalId, 0, 10);

        // then
        List<TodoSummary> todos = result.content();

        assertThat(todos).hasSize(6); // 완료되지 않은 것 4 + 오늘인 것만 2
        assertThat(todos)
                .extracting(TodoSummary::title)
                .containsExactly("어제 미완료 투두", "오늘 미완료 투두", "내일 미완료 투두", "미완료 투두 날짜없음",
                        "오늘 완료 투두 결과없음", "오늘 완료 투두 결과있음");
    }

    @Test
    void findAllByUserId_정렬조건과_페이징이_적용된다() {
        // when
        CustomSlice<TodoSummary> result = todoRepository.findAllByUserId(userId, 0, 4);

        // then
        List<TodoSummary> todos = result.content();

        assertThat(todos).hasSize(4);
        assertThat(result.hasNext()).isTrue(); // 총 8개 → 다음 페이지 존재

        // 정렬: completed false 우선 → 날짜 빠른 순 → null은 마지막
        assertThat(todos)
                .extracting(TodoSummary::title)
                .containsExactly("어제 미완료 투두", "오늘 미완료 투두", "다른 세부목표 오늘 미완료투두", "내일 미완료 투두");
    }
}

