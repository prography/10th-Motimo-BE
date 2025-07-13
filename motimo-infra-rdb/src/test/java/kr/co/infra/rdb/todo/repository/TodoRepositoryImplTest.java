package kr.co.infra.rdb.todo.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.goal.DueDate;
import kr.co.domain.goal.dto.GoalTodoCount;
import kr.co.domain.todo.Emotion;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.TodoStatus;
import kr.co.domain.todo.dto.TodoItemDto;
import kr.co.domain.todo.exception.TodoNotFoundException;
import kr.co.domain.todo.repository.TodoRepository;
import kr.co.infra.rdb.config.QueryDslConfig;
import kr.co.infra.rdb.goal.entity.DueDateEmbeddable;
import kr.co.infra.rdb.goal.entity.GoalEntity;
import kr.co.infra.rdb.subGoal.entity.SubGoalEntity;
import kr.co.infra.rdb.todo.entity.TodoEntity;
import kr.co.infra.rdb.todo.entity.TodoResultEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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
    private TestEntityManager testEntityManager;

    private JPAQueryFactory jpaQueryFactory;

    private UUID userId;
    private UUID goalId;
    private UUID subGoalId;
    private LocalDate today;
    private LocalDate yesterday;
    private LocalDate tomorrow;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

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
        goalId = createGoal("또 다른 목표", userId);
        subGoalId = createSubGoal("또 다른 세부목표", goalId);

        // 완료되지 않은 투두
        createTodo("오늘 미완료 투두", TodoStatus.INCOMPLETE, today, subGoalId, userId, false);
        createTodo("내일 미완료 투두", TodoStatus.INCOMPLETE, tomorrow, subGoalId, userId, false);
        createTodo("어제 미완료 투두", TodoStatus.INCOMPLETE, yesterday, subGoalId, userId, false);
        createTodo("미완료 투두 날짜없음", TodoStatus.INCOMPLETE, null, subGoalId, userId, false);

        // 오늘 완료된 투두 (우선순위: 1 - 결과 없음, 우선순위: 2 - 결과 있음)
        createTodo("오늘 완료 투두 결과없음", TodoStatus.COMPLETE, today, subGoalId, userId, false);
        createTodo("오늘 완료 투두 결과있음", TodoStatus.COMPLETE, today, subGoalId, userId, true);

        // 과거에 완료된 투두
        createTodo("과거 완료 투두", TodoStatus.COMPLETE, yesterday, subGoalId, userId, false);

        // 다른 세부목표의 투두
        UUID otherGoalId = createGoal("다른 목표", userId);
        UUID otherSubGoalId = createSubGoal("다른 세부목표", otherGoalId);
        createTodo("다른 세부목표 오늘 미완료투두", TodoStatus.INCOMPLETE, today, otherSubGoalId, userId, false);
        createTodo("다른 세부목표 오늘 완료투두", TodoStatus.COMPLETE, today, otherSubGoalId, userId, false);

        // 다른 유저의 투두
        UUID otherUserId = UUID.randomUUID();
        createTodo("다른 유저 투두", TodoStatus.INCOMPLETE, today, otherSubGoalId, otherUserId, false);
    }

    private UUID createGoal(String title, UUID userId) {
        GoalEntity goal = new GoalEntity(
                null,
                userId,
                title,
                DueDateEmbeddable.from(DueDate.of(LocalDate.now().plusDays(30))),
                false
        );
        testEntityManager.persist(goal);
        testEntityManager.flush();
        return goal.getId();
    }

    private UUID createSubGoal(String title, UUID goalId) {
        GoalEntity goal = testEntityManager.find(GoalEntity.class, goalId);
        SubGoalEntity subGoal = new SubGoalEntity(
                goal,
                null,
                title,
                1
        );
        testEntityManager.persist(subGoal);
        testEntityManager.flush();
        return subGoal.getId();
    }

    private void createTodo(String title, TodoStatus status, LocalDate date,
            UUID subGoalId, UUID userId, boolean hasResult) {

        TodoEntity todo = new TodoEntity(
                null,  // id - 자동 생성됨
                subGoalId,
                userId,  // userId
                title,
                date,
                status
        );

        testEntityManager.persist(todo);

        if (hasResult) {
            testEntityManager.flush();
            TodoResultEntity result = new TodoResultEntity(
                    null,
                    todo.getId(),
                    userId,
                    Emotion.PROUD,
                    "투두 완료!",
                    "image"
            );
            testEntityManager.persist(result);
        }
        testEntityManager.flush();
    }

    @Test
    void 완료되지않은_상태거나_오늘의_투두들만_조회되고_정렬된다() {
        // when
        CustomSlice<TodoItemDto> todos = todoRepository.findIncompleteOrDateTodosBySubGoalId(
                subGoalId,
                today, 0, 10);

        // then
        assertThat(todos.content()).hasSize(6); // 완료되지 않은 것 4 + 오늘인 것만 2
        assertThat(todos.content())
                .extracting(TodoItemDto::title)
                .containsExactly("어제 미완료 투두", "오늘 미완료 투두", "오늘 완료 투두 결과없음", "오늘 완료 투두 결과있음",
                        "내일 미완료 투두", "미완료 투두 날짜없음");
    }

    @Test
    void 투두_생성_테스트() {
        // given
        Todo todo = Todo.builder()
                .subGoalId(subGoalId)
                .userId(userId)
                .title("새로운 투두")
                .date(today)
                .status(TodoStatus.INCOMPLETE)
                .build();

        // when
        Todo savedTodo = todoRepository.create(todo);

        // then
        assertThat(savedTodo.getId()).isNotNull();
        assertThat(savedTodo.getTitle()).isEqualTo("새로운 투두");
        assertThat(savedTodo.getStatus()).isEqualTo(TodoStatus.INCOMPLETE);
        assertThat(savedTodo.getDate()).isEqualTo(today);
        assertThat(savedTodo.getSubGoalId()).isEqualTo(subGoalId);
        assertThat(savedTodo.getUserId()).isEqualTo(userId);
    }

    @Test
    void ID로_투두_조회_테스트() {
        // given
        TodoEntity todoEntity = new TodoEntity(null, subGoalId, userId, "조회 테스트", today,
                TodoStatus.INCOMPLETE);
        testEntityManager.persist(todoEntity);
        testEntityManager.flush();

        // when
        Todo foundTodo = todoRepository.findById(todoEntity.getId());

        // then
        assertThat(foundTodo).isNotNull();
        assertThat(foundTodo.getId()).isEqualTo(todoEntity.getId());
        assertThat(foundTodo.getTitle()).isEqualTo("조회 테스트");
        assertThat(foundTodo.getStatus()).isEqualTo(TodoStatus.INCOMPLETE);
    }

    @Test
    void 존재하지_않는_ID로_조회시_예외_발생() {
        // given
        UUID nonExistentId = UUID.randomUUID();

        // when & then
        assertThatThrownBy(() -> todoRepository.findById(nonExistentId))
                .isInstanceOf(TodoNotFoundException.class);
    }

    @Test
    void 유저_ID로_모든_투두_조회() {
        // when
        List<TodoItemDto> todos = todoRepository.findAllByUserId(userId);

        // then
        assertThat(todos).hasSize(9); // setupTestData()에서 생성한 해당 유저의 투두 개수
        assertThat(todos)
                .extracting(TodoItemDto::title)
                .contains("오늘 미완료 투두", "오늘 완료 투두 결과있음", "다른 세부목표 오늘 미완료투두");
    }

    @Test
    void 세부목표_ID로_모든_투두_조회() {
        // when
        List<TodoItemDto> todos = todoRepository.findAllBySubGoalId(subGoalId);

        // then
        assertThat(todos).hasSize(7); // setupTestData()에서 생성한 해당 세부목표의 투두 개수
        assertThat(todos)
                .extracting(TodoItemDto::title)
                .contains("오늘 미완료 투두", "오늘 완료 투두 결과있음", "과거 완료 투두");
    }

    @Test
    void 투두_존재_여부_확인() {
        // given
        TodoEntity todoEntity = new TodoEntity(null, subGoalId, userId, "존재 확인 테스트", today,
                TodoStatus.INCOMPLETE);
        testEntityManager.persist(todoEntity);
        testEntityManager.flush();

        // when & then
        assertThat(todoRepository.existsById(todoEntity.getId())).isTrue();
        assertThat(todoRepository.existsById(UUID.randomUUID())).isFalse();
    }

    @Test
    void 투두_업데이트_테스트() {
        // given
        TodoEntity todoEntity = new TodoEntity(null, subGoalId, userId, "업데이트 전", today,
                TodoStatus.INCOMPLETE);
        testEntityManager.persist(todoEntity);
        testEntityManager.flush();

        Todo updateTodo = Todo.builder()
                .id(todoEntity.getId())
                .title("업데이트 후")
                .date(tomorrow)
                .status(TodoStatus.COMPLETE)
                .build();

        // when
        Todo updatedTodo = todoRepository.update(updateTodo);

        // then
        assertThat(updatedTodo.getTitle()).isEqualTo("업데이트 후");
        assertThat(updatedTodo.getDate()).isEqualTo(tomorrow);
        assertThat(updatedTodo.getStatus()).isEqualTo(TodoStatus.COMPLETE);
    }

    @Test
    void 존재하지_않는_투두_업데이트시_예외_발생() {
        // given
        Todo nonExistentTodo = Todo.builder()
                .id(UUID.randomUUID())
                .title("존재하지 않는 투두")
                .date(today)
                .status(TodoStatus.INCOMPLETE)
                .build();

        // when & then
        assertThatThrownBy(() -> todoRepository.update(nonExistentTodo))
                .isInstanceOf(TodoNotFoundException.class);
    }

    @Test
    void 투두_삭제_테스트() {
        // given
        TodoEntity todoEntity = new TodoEntity(null, subGoalId, userId, "삭제 테스트", today,
                TodoStatus.INCOMPLETE);
        testEntityManager.persist(todoEntity);
        testEntityManager.flush();

        UUID todoId = todoEntity.getId();

        // when
        todoRepository.deleteById(todoId);

        // then
        assertThat(todoJpaRepository.existsById(todoId)).isFalse();
    }

    @Test
    void 목표_ID들로_투두_개수_조회() {
        // given
        List<UUID> goalIds = List.of(goalId);

        // when
        List<GoalTodoCount> todoCountList = todoRepository.countTodosByGoalIds(goalIds);

        // then
        assertThat(todoCountList).hasSize(1);

        GoalTodoCount todoCount = todoCountList.getFirst();
        assertThat(todoCount.goalId()).isEqualTo(goalId);
        assertThat(todoCount.todoCount()).isEqualTo(7L);
        assertThat(todoCount.todoResultCount()).isEqualTo(1L);
    }

    @Test
    void 여러_목표_ID들로_투두_개수_조회() {
        // given
        UUID anotherGoalId = createGoal("또 다른 목표", userId);
        UUID anotherSubGoalId = createSubGoal("또 다른 세부목표", anotherGoalId);
        createTodo("또 다른 투두1", TodoStatus.INCOMPLETE, today, anotherSubGoalId, userId, false);
        createTodo("또 다른 투두2", TodoStatus.COMPLETE, today, anotherSubGoalId, userId, true);

        List<UUID> goalIds = List.of(goalId, anotherGoalId);

        // when
        List<GoalTodoCount> todoCountList = todoRepository.countTodosByGoalIds(goalIds);

        // then
        assertThat(todoCountList).hasSize(2);

        Map<UUID, GoalTodoCount> countMap = todoCountList.stream()
                .collect(Collectors.toMap(GoalTodoCount::goalId, Function.identity()));

        assertThat(countMap).containsKeys(goalId, anotherGoalId);
        assertThat(countMap.get(goalId).todoCount()).isEqualTo(7L);
        assertThat(countMap.get(anotherGoalId).todoCount()).isEqualTo(2L);
        assertThat(countMap.get(anotherGoalId).todoResultCount()).isEqualTo(1L);
    }

    @Test
    void 빈_목표_ID_리스트로_투두_개수_조회시_빈_결과_반환() {
        // given
        List<UUID> emptyGoalIds = List.of();

        // when
        List<GoalTodoCount> todoCountList = todoRepository.countTodosByGoalIds(emptyGoalIds);

        // then
        assertThat(todoCountList).isEmpty();
    }

    @Test
    void 투두_조회시_정렬_확인() {
        // when
        List<TodoItemDto> todos = todoRepository.findAllBySubGoalId(subGoalId);

        // then
        assertThat(todos.getFirst().title()).isEqualTo("어제 미완료 투두");
        assertThat(todos.getFirst().date()).isEqualTo(yesterday);

        List<TodoItemDto> todayTodos = todos.stream()
                .filter(todo -> today.equals(todo.date()))
                .collect(Collectors.toList());

        assertThat(todayTodos).hasSize(3);
        assertThat(todayTodos.getFirst().title()).isEqualTo("오늘 미완료 투두");
    }

    @Test
    void 투두_결과_정보_포함_조회() {
        // when
        List<TodoItemDto> todos = todoRepository.findAllBySubGoalId(subGoalId);

        // then
        TodoItemDto todoWithResult = todos.stream()
                .filter(todo -> "오늘 완료 투두 결과있음".equals(todo.title()))
                .findFirst()
                .orElseThrow();

        assertThat(todoWithResult.todoResultItem()).isNotNull();
        assertThat(todoWithResult.todoResultItem().content()).isEqualTo("투두 완료!");
        assertThat(todoWithResult.todoResultItem().emotion()).isEqualTo(Emotion.PROUD);
    }
}

