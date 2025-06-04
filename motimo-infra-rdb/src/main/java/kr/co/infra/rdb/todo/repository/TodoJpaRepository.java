package kr.co.infra.rdb.todo.repository;

import kr.co.infra.rdb.todo.entity.TodoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;

public interface TodoJpaRepository extends JpaRepository<TodoEntity, UUID> {

    // TODO: query 더 최적화해보기...
    @Query(value = """
            SELECT * FROM todo t 
            WHERE t.sub_goal_id = :subGoalId 
            AND (t.completed = false OR t.date = :today) 
            ORDER BY 
                CASE 
                    WHEN t.date = :today THEN 0
                    ELSE ABS(t.date - :today)
                END ASC
            """, nativeQuery = true)
    Slice<TodoEntity> findAllBySubGoalIdForTodayOrIncomplete(@Param("subGoalId") UUID subGoalId,
            @Param("today") LocalDate today, Pageable pageable);

    Slice<TodoEntity> findAllBySubGoalId(UUID subGoalId, Pageable pageable);

    Slice<TodoEntity> findAllByAuthorId(UUID userId, Pageable pageable);
}
