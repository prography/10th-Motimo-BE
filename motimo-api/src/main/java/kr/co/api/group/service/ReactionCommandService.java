package kr.co.api.group.service;

import java.util.UUID;
import kr.co.domain.group.reaction.Reaction;
import kr.co.domain.group.reaction.ReactionType;
import kr.co.domain.group.reaction.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReactionCommandService {

    private final ReactionRepository reactionRepository;

    public UUID upsertReaction(UUID userId, UUID messageId, ReactionType type) {
        // TODO 그룹 메시지 발행

        Reaction upsertedReaction = reactionRepository.upsert(Reaction.builder()
                        .userId(userId)
                        .messageId(messageId)
                        .reactionType(type).build());

        // 첫번째 남기는 리액션이라면
//        if (true) {
//            Events.publishEvent(new TodoReactionFirstCreatedEvent(userId, type, ));
//        }

        return upsertedReaction.getMessageId();
    }

    public void deleteReaction(UUID userId, UUID messageId) {
        reactionRepository.deleteByUserIdAndMessageId(userId, messageId);
    }
}
