package kr.co.infra.rdb.group.message.repostiory;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.group.message.GroupMessage;
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
    public CustomSlice<GroupMessage> findAllByGroupId(UUID groupId, int offset, int limit) {

        QGroupMessageEntity messageEntity = QGroupMessageEntity.groupMessageEntity;
        QReactionEntity reactionEntity = QReactionEntity.reactionEntity;

        // 그룹 메시지 조회 (size+1 로 초과 조회 → hasNext 계산)
        List<GroupMessageEntity> messages = jpaQueryFactory
                .selectFrom(messageEntity)
                .where(messageEntity.groupId.eq(groupId))
                .orderBy(messageEntity.sendAt.desc())
                .offset(offset)
                .limit(limit + 1)
                .fetch();

        boolean hasNext = messages.size() > limit;

        if (hasNext) {
            messages = messages.subList(0, limit);
        }

        // 메시지가 없으면 빈 결과 반환
        if (messages.isEmpty()) {
            return new CustomSlice<>(List.of(), false);
        }

        // 메시지 ID들로 리액션 한 번에 조회
        List<UUID> messageIds = messages.stream()
                .map(GroupMessageEntity::getId)
                .toList();

        List<ReactionEntity> reactions = jpaQueryFactory
                .selectFrom(reactionEntity)
                .where(reactionEntity.messageId.in(messageIds))
                .orderBy(reactionEntity.createdAt.asc()) // 리액션 순서 정렬
                .fetch();

        // 메시지ID별로 리액션 그룹핑
        Map<UUID, List<ReactionEntity>> reactionsMap = reactions.stream()
                .collect(Collectors.groupingBy(ReactionEntity::getMessageId));

        // 도메인 객체로 변환
        List<GroupMessage> result = messages.stream()
                .map(msgEntity -> {
                    List<Reaction> messageReactions = reactionsMap
                            .getOrDefault(msgEntity.getId(), List.of())
                            .stream().map(ReactionMapper::toDomain)
                            .toList();
                    return GroupMessageMapper.toDomainWithReactions(msgEntity, messageReactions);
                })
                .toList();

        return new CustomSlice<>(result, hasNext);
    }
}
