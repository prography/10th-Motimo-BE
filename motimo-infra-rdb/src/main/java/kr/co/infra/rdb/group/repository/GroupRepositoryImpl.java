package kr.co.infra.rdb.group.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kr.co.domain.group.Group;
import kr.co.domain.group.GroupMember;
import kr.co.domain.group.dto.GroupJoinDto;
import kr.co.domain.group.exception.GroupNotFoundException;
import kr.co.domain.group.repository.GroupRepository;
import kr.co.infra.rdb.goal.entity.QGoalEntity;
import kr.co.infra.rdb.group.entity.GroupEntity;
import kr.co.infra.rdb.group.entity.GroupMemberEntity;
import kr.co.infra.rdb.group.entity.QGroupEntity;
import kr.co.infra.rdb.group.entity.QGroupMemberEntity;
import kr.co.infra.rdb.group.repository.projection.GroupMemberGoalGroupProjection;
import kr.co.infra.rdb.group.repository.query.GroupJpaSubQuery;
import kr.co.infra.rdb.group.util.GroupMapper;
import kr.co.infra.rdb.group.util.GroupMemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GroupRepositoryImpl implements GroupRepository {

    private final GroupJpaRepository groupJpaRepository;
    private final GroupMemberJpaRepository groupMemberJpaRepository;

    private final JPAQueryFactory jpaQueryFactory;
    private final GroupJpaSubQuery groupJpaQuery;

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
                        dto.userId(),
                        dto.goalId(),
                        groupEntity,
                        LocalDateTime.now()
                )
        );

        return GroupMapper.toDomain(groupEntity);
    }

    public void left(UUID groupId, GroupMember member) {
        groupMemberJpaRepository.deleteByGroupIdAndUserId(groupId, member.getMemberId());
    }

    public Group findById(UUID groupId) {
        GroupEntity groupEntity = groupJpaRepository.findById(groupId)
                .orElseThrow(GroupNotFoundException::new);

        List<GroupMember> members = groupMemberJpaRepository.findByGroupId(groupId).stream().map(GroupMemberMapper::toDomain).toList();

        return GroupMapper.toDomain(groupEntity, members);
    }

    public Group findByIdWithoutMembers(UUID groupId) {
        GroupEntity groupEntity = groupJpaRepository.findById(groupId)
                .orElseThrow(GroupNotFoundException::new);
        return GroupMapper.toDomain(groupEntity);
    }

    public Group findDetailByMemberIdAndGroupId(UUID memberId, UUID groupId) {
        GroupMemberGoalGroupProjection projection = groupMemberJpaRepository.findGoalAndGroupByUserIdAndGroupId(memberId, groupId).orElseThrow(GroupNotFoundException::new);

        return GroupMapper.toDomainWithName(projection.getGroupId(), projection.getGoalTitle(), projection.getGroupFinishedDate());
    }

    public Optional<Group> findByGoalId(UUID goalId) {
        Optional<GroupMemberEntity> groupMemberEntity = groupMemberJpaRepository.findByGoalId(
                goalId);

        return groupMemberEntity.map(memberEntity -> {
            GroupEntity groupEntity = groupJpaRepository.findById(memberEntity.getGroupId())
                    .orElseThrow(GroupNotFoundException::new);

            return GroupMapper.toDomain(groupEntity);
        });
    }

    public Optional<Group> findAvailableGroupBySimilarDueDate(UUID userId, LocalDate dueDate) {
        QGroupEntity group = QGroupEntity.groupEntity;
        QGroupMemberEntity member = QGroupMemberEntity.groupMemberEntity;
        QGoalEntity goal = QGoalEntity.goalEntity;

        JPAQuery<Long> memberCountSubquery = groupJpaQuery.memberCountSubquery(group, member);

        GroupEntity result = jpaQueryFactory
                .selectFrom(group)
                .where(
                        memberCountSubquery.lt((long) Group.MAX_GROUP_MEMBER_COUNT),

                        groupJpaQuery.userNotInGroup(userId, group, member),

                        groupJpaQuery.hasSimilarDueDate(dueDate, group, member, goal)
                )
                .limit(1)
                .fetchOne();

        return result == null ? Optional.empty() : Optional.of(GroupMapper.toDomain(result));
    }

    public List<Group> findAllGroupDetailByUserId(UUID userId) {
        List<GroupMemberGoalGroupProjection> groupProjections = groupMemberJpaRepository.findAllGoalAndGroupByUserId(
                userId);

        return groupProjections.stream()
                .map(projection -> GroupMapper.toDomainWithName(projection.getGroupId(),
                        projection.getGoalTitle(), projection.getGroupFinishedDate())).toList();
    }

    public boolean existsByGoalId(UUID goalId) {
        return groupMemberJpaRepository.existsByGoalId(goalId);
    }

}
