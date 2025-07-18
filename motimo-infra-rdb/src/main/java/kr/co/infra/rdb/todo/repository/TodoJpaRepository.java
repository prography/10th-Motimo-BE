package kr.co.infra.rdb.todo.repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import kr.co.infra.rdb.todo.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoJpaRepository extends JpaRepository<TodoEntity, UUID> {

    List<TodoEntity> findAllByIdIn(Set<UUID> todoIds);

    void deleteAllBySubGoalId(UUID subGoalIds);
}
