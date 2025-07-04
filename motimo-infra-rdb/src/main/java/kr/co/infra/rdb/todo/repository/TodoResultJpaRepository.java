package kr.co.infra.rdb.todo.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import kr.co.infra.rdb.todo.entity.TodoResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoResultJpaRepository extends JpaRepository<TodoResultEntity, UUID> {

    Optional<TodoResultEntity> findByTodoId(UUID todoId);

    List<TodoResultEntity> findAllByIdIn(Set<UUID> todoResultIds);
}
