package kr.co.infra.rdb.group.repository.query;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.UUID;
import kr.co.infra.rdb.goal.entity.QGoalEntity;
import kr.co.infra.rdb.group.entity.QGroupEntity;
import kr.co.infra.rdb.group.entity.QGroupMemberEntity;
import org.springframework.stereotype.Component;

@Component
public class GroupJpaSubQuery {

    private final JPAQueryFactory jpaQueryFactory;

    public GroupJpaSubQuery(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public JPAQuery<Long> memberCountSubquery(QGroupEntity group, QGroupMemberEntity member) {
        return jpaQueryFactory
                .select(member.count())
                .from(member)
                .where(member.group.id.eq(group.id));
    }

    public BooleanExpression userNotInGroup(UUID userId, QGroupEntity group,
            QGroupMemberEntity member) {
        return group.id.notIn(
                JPAExpressions
                        .select(member.group.id)
                        .from(member)
                        .where(member.userId.eq(userId))
        );
    }

    public BooleanExpression hasSimilarDueDate(LocalDate dueDate, QGroupEntity group,
            QGroupMemberEntity member, QGoalEntity goal) {
        int targetDayOfYear = dueDate.getDayOfYear();

        return Expressions.numberTemplate(Double.class,
                "ABS({0} - {1})",
                targetDayOfYear,
                avgDueDateSubquery(group, member, goal)
        ).loe(30.0);
    }

    private NumberExpression<Double> avgDueDateSubquery(QGroupEntity group,
            QGroupMemberEntity member, QGoalEntity goal) {
        var subquery = JPAExpressions
                .select(extractDayOfYear(goal.dueDate.dueDate).avg())
                .from(member)
                .join(goal).on(member.goalId.eq(goal.id))
                .where(member.group.id.eq(group.id));

        return Expressions.numberTemplate(Double.class, "({0})", subquery);
    }

    private NumberExpression<Integer> extractDayOfYear(Path<? extends LocalDate> datePath) {
        // PostgreSQL의 date_part 함수를 직접 사용
        return Expressions.numberTemplate(Integer.class,
                "CAST(date_part('doy', {0}) AS INTEGER)", datePath);
    }

}