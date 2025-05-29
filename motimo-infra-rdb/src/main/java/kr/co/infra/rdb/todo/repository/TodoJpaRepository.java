package kr.co.infra.rdb.todo.repository;

import kr.co.infra.rdb.todo.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TodoJpaRepository extends JpaRepository<TodoEntity, UUID> {
    List<TodoEntity> findAllBySubGoalId(UUID subGoalId);

    /** todo: query 수정
    @Query("SELECT t FROM TodoEntity t " +
            "WHERE t.subGoalId = :subGoalId AND (t.completed = false OR t.date = :today) " +
            "ORDER BY abs(DATEDIFF(t.date, :today)) ASC")
    Page<TodoEntity> findAllBySubGoalIdForTodayAndIncomplete(@Param("subGoalId") Long subGoalId,
                                                    @Param("today") LocalDate today, Pageable pageable);
    **/
}
