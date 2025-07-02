package kr.co.infra.rdb.group.sql;

import org.springframework.stereotype.Component;

@Component
public class GroupSqlBuilder {

    private final static int maxMemberCount = 6;
    private final static int similarDate = 30;

    public static String buildFindGroupBySimilarDueDateQuery() {
        return """
            SELECT g.* FROM groups g
            WHERE g.is_deleted = false
            AND (
                SELECT COUNT(*) FROM group_users gm 
                WHERE gm.group_id = g.id AND gm.is_deleted = false
            ) < %d
            AND g.id NOT IN (
                SELECT gm2.group_id FROM group_users gm2 
                WHERE gm2.user_id = :userId AND gm2.is_deleted = false
            )
            AND EXISTS (
                SELECT 1 FROM (
                    SELECT AVG(EXTRACT(DAY FROM (goal.due_date - :dueDate::date))) as avg_diff
                    FROM group_users gm3
                    JOIN goals goal ON gm3.goal_id = goal.id
                    WHERE gm3.group_id = g.id 
                    AND gm3.is_deleted = false 
                    AND goal.is_deleted = false
                ) sub 
                WHERE ABS(sub.avg_diff) <= %d
            )
            LIMIT 1
            """.formatted(maxMemberCount, similarDate);
    }
}
