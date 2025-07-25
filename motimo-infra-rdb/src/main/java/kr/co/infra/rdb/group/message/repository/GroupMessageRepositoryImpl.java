package kr.co.infra.rdb.group.message.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import kr.co.domain.common.pagination.CursorResult;
import kr.co.domain.common.pagination.CustomCursor;
import kr.co.domain.common.pagination.PagingDirection;
import kr.co.domain.group.message.GroupMessage;
import kr.co.domain.group.message.NewGroupMessages;
import kr.co.domain.group.message.repository.GroupMessageRepository;
import kr.co.domain.group.reaction.Reaction;
import kr.co.infra.rdb.group.message.GroupMessageEntity;
import kr.co.infra.rdb.group.message.GroupMessageMapper;
import kr.co.infra.rdb.group.message.QGroupMessageEntity;
import kr.co.infra.rdb.group.reaction.QReactionEntity;
import kr.co.infra.rdb.group.reaction.ReactionEntity;
import kr.co.infra.rdb.group.reaction.ReactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GroupMessageRepositoryImpl implements GroupMessageRepository {

    private final GroupMessageJpaRepository groupMessageJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public GroupMessage create(GroupMessage groupMessage) {
        GroupMessageEntity entity = groupMessageJpaRepository.save(
                GroupMessageMapper.toEntity(groupMessage));
        return GroupMessageMapper.toDomain(entity);
    }

    @Override
    public CursorResult<GroupMessage> findAllByGroupIdWithCursor(
            UUID groupId, CustomCursor cursor, int limit, PagingDirection direction) {

        QGroupMessageEntity messageEntity = QGroupMessageEntity.groupMessageEntity;

        // 쿼리 조건 구성
        BooleanBuilder whereCondition = createGroupMessageWhereConditionByCursor(
                groupId, cursor, direction, messageEntity);

        OrderSpecifier<?>[] orderSpecifiers = (direction == PagingDirection.BEFORE)
                ? new OrderSpecifier[]{
                messageEntity.sendAt.desc().nullsLast(),
                messageEntity.id.desc()
        }
                : new OrderSpecifier[]{
                        messageEntity.sendAt.asc().nullsLast(),
                        messageEntity.id.asc()
                };

        // 메시지 조회 (limit + 1로 더 많은 데이터 확인)
        List<GroupMessageEntity> messages = jpaQueryFactory
                .selectFrom(messageEntity)
                .where(whereCondition)
                .orderBy(orderSpecifiers)
                .limit(limit + 1)
                .fetch();

        // hasAfter / hasBefore 계산
        boolean hasMore = messages.size() > limit;
        if (hasMore) {
            messages = messages.subList(0, limit);
        }

        if (direction == PagingDirection.AFTER) {
            Collections.reverse(messages);
        }

        // 방향에 따른 hasAfter/hasBefore 설정
        boolean hasBefore = direction == PagingDirection.BEFORE ? hasMore : cursor != null;
        boolean hasAfter = direction == PagingDirection.AFTER ? hasMore : cursor != null;

        List<GroupMessage> result = processMessagesWithReactions(messages);

        return new CursorResult<>(result, hasBefore, hasAfter);
    }

    @Override
    public NewGroupMessages findNewMessagesFromLatestDate(UUID groupId, LocalDateTime latestDate,
            UUID latestMessageId) {
        QGroupMessageEntity messageEntity = QGroupMessageEntity.groupMessageEntity;

        Tuple result = jpaQueryFactory
                .select(
                        messageEntity.count(),
                        messageEntity.sendAt.max(),
                        messageEntity.id.max()
                )
                .from(messageEntity)
                .where(messageEntity.groupId.eq(groupId)
                        .and(messageEntity.sendAt.after(latestDate)
                                .or(messageEntity.sendAt.eq(latestDate)
                                        .and(messageEntity.id.gt(latestMessageId)))))
                .fetchOne();

        if (result == null || result.get(messageEntity.count()) == null) {
            return new NewGroupMessages(0, null, null);
        }

        return new NewGroupMessages(
                Optional.ofNullable(result.get(messageEntity.count())).orElse(0L).intValue(),
                result.get(messageEntity.sendAt.max()),
                result.get(messageEntity.id.max())
        );
    }

    @Override
    public void deleteAllByReferenceId(UUID referenceId) {
        groupMessageJpaRepository.deleteAllByMessageReferenceReferenceId(referenceId);
    }

    @Override
    public Optional<GroupMessage> findLastGroupMessageByGroupId(UUID groupId) {
        Optional<GroupMessageEntity> groupMessageEntity = groupMessageJpaRepository.findTopByGroupIdOrderBySendAtDesc(
                groupId);

        return groupMessageEntity.map(GroupMessageMapper::toDomain);
    }

    @Override
    public Optional<GroupMessage> findById(UUID id) {
        Optional<GroupMessageEntity> entity = groupMessageJpaRepository.findById(id);

        return entity.map(GroupMessageMapper::toDomain);
    }

    @Override
    public void deleteAllByUserId(UUID userId) {
        groupMessageJpaRepository.deleteAllByUserId(userId);
    }

    private List<GroupMessage> processMessagesWithReactions(List<GroupMessageEntity> messages) {
        if (messages.isEmpty()) {
            return List.of();
        }

        // 메시지 ID들로 리액션 한 번에 조회
        List<UUID> messageIds = messages.stream()
                .map(GroupMessageEntity::getId)
                .toList();

        QReactionEntity reactionEntity = QReactionEntity.reactionEntity;
        List<ReactionEntity> reactions = jpaQueryFactory
                .selectFrom(reactionEntity)
                .where(reactionEntity.messageId.in(messageIds))
                .orderBy(reactionEntity.createdAt.asc())
                .fetch();

        // 메시지ID별로 리액션 그룹핑
        Map<UUID, List<ReactionEntity>> reactionsMap = reactions.stream()
                .collect(Collectors.groupingBy(ReactionEntity::getMessageId));

        // 도메인 객체로 변환
        return messages.stream()
                .map(msgEntity -> {
                    List<Reaction> messageReactions = reactionsMap
                            .getOrDefault(msgEntity.getId(), List.of())
                            .stream()
                            .map(ReactionMapper::toDomain)
                            .toList();
                    return GroupMessageMapper.toDomainWithReactions(msgEntity, messageReactions);
                })
                .toList();
    }

    private BooleanBuilder createGroupMessageWhereConditionByCursor(UUID groupId,
            CustomCursor cursor, PagingDirection direction, QGroupMessageEntity messageEntity) {
        BooleanBuilder whereCondition = new BooleanBuilder();
        whereCondition.and(messageEntity.groupId.eq(groupId));

        if (cursor != null) {
            if (direction == PagingDirection.BEFORE) {
                // 이전 메시지 (과거 메시지)
                whereCondition.and(
                        messageEntity.sendAt.lt(cursor.dateTime())
                                .or(messageEntity.sendAt.eq(cursor.dateTime())
                                        .and(messageEntity.id.lt(cursor.id())))
                );
            } else {
                // 이후 메시지 (최신 메시지)
                whereCondition.and(
                        messageEntity.sendAt.gt(cursor.dateTime())
                                .or(messageEntity.sendAt.eq(cursor.dateTime())
                                        .and(messageEntity.id.gt(cursor.id())))
                );
            }
        }
        return whereCondition;
    }
}
