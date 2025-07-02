package kr.co.infra.rdb.group.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import kr.co.domain.group.Group;
import kr.co.domain.group.dto.GroupJoinDto;
import kr.co.domain.group.repository.GroupRepository;
import kr.co.infra.rdb.goal.entity.QGoalEntity;
import kr.co.infra.rdb.group.entity.GroupEntity;
import kr.co.infra.rdb.group.entity.GroupMapper;
import kr.co.infra.rdb.group.entity.GroupMemberEntity;
import kr.co.infra.rdb.group.entity.QGroupEntity;
import kr.co.infra.rdb.group.entity.QGroupMemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GroupRepositoryImpl implements GroupRepository {

    private final GroupJpaRepository groupJpaRepository;
    private static final long MAX_GROUP_COUNT = 6;
    private final GroupMemberJpaRepository groupMemberJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;

    public Group create(Group group) {
        GroupEntity groupEntity = groupJpaRepository.save(
                GroupMapper.toEntity(group)
        );
        return GroupMapper.toDomain(groupEntity);
    }

    public Group join(GroupJoinDto dto) {
        GroupEntity groupEntity = groupJpaRepository.findById(dto.groupId()).orElseThrow();

        groupMemberJpaRepository.save(
                new GroupMemberEntity(
                        null,
                       dto.userId(),
                        dto.goalId(),
                        groupEntity,
                        LocalDateTime.now()
                )
        );

        return GroupMapper.toDomain(groupEntity);
    }

    // dueDate와 차이 30일 이내 + 6인 이내 그룹 응답
    public Optional<Group> findAvailableGroupBySimilarDueDate(UUID userId, LocalDate dueDate) {
        QGroupEntity group = QGroupEntity.groupEntity;
        QGroupMemberEntity member = QGroupMemberEntity.groupMemberEntity;
        QGoalEntity goal = QGoalEntity.goalEntity;

        // 각 그룹 멤버 수 계산
        JPAQuery<Long> memberCountSubquery = jpaQueryFactory
                .select(member.count())
                .from(member)
                .where(member.group.id.eq(group.id));

        // 각 그룹 멤버들의 목표 완료날짜 평균을 계산
        JPAQuery<Double> avgDueDateSubquery = jpaQueryFactory
                .select(goal.dueDate.dueDate.dayOfYear().avg())
                .from(member)
                .join(goal).on(member.goalId.eq(goal.id))
                .where(member.group.id.eq(group.id));

        GroupEntity result = jpaQueryFactory
                .selectFrom(group)
                .where(
                        // 1. 멤버 수가 6명 미만인 그룹
                        memberCountSubquery.lt(MAX_GROUP_COUNT),

                        // 2. 해당 사용자가 이미 속하지 않은 그룹
                        group.id.notIn(
                                JPAExpressions
                                        .select(member.group.id)
                                        .from(member)
                                        .where(member.userId.eq(userId))
                        ),

                        // 3. 평균 완료날짜와 30일 이내 차이
                        Expressions.numberTemplate(Double.class,
                                "ABS({0} - {1})",
                                dueDate.getDayOfYear(),
                                avgDueDateSubquery
                        ).loe(30.0)
                )
                .limit(1)
                .fetchOne();

        return Optional.ofNullable(GroupMapper.toDomain(result));
    }

}
