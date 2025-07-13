package kr.co.infra.rdb.group.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kr.co.infra.rdb.group.entity.GroupMemberEntity;
import kr.co.infra.rdb.group.repository.projection.GroupMemberGoalGroupProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupMemberJpaRepository extends JpaRepository<GroupMemberEntity, UUID> {

    boolean existsByGoalId(UUID goalId);

    void deleteByGroupIdAndUserId(UUID groupId, UUID userId);

    Optional<GroupMemberEntity> findByGoalId(UUID goalId);

    List<GroupMemberEntity> findByUserId(UUID userId);

    @Query(value = """
                SELECT 
                    gm.goal_id AS goalId,
                    g.title AS goalTitle,
                    gm.group_id AS groupId,
                    gr.finished_date AS groupFinishedDate
                FROM group_members gm
                JOIN goals g ON gm.goal_id = g.id
                JOIN goal_groups gr ON gm.group_id = gr.id
                WHERE gm.user_id = :userId AND gm.is_deleted = false
            """, nativeQuery = true)
    List<GroupMemberGoalGroupProjection> findGoalAndGroupInfoByUserId(@Param("userId") UUID userId);

}
