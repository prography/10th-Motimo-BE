package kr.co.api.group.service;

import java.util.UUID;
import kr.co.domain.common.event.Events;
import kr.co.domain.common.event.group.message.MessageReactionFirstCreatedEvent;
import kr.co.domain.group.reaction.Reaction;
import kr.co.domain.group.reaction.ReactionDomainId;
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
        boolean existsBefore = reactionRepository.existsById(ReactionDomainId.of(userId, messageId));

        Reaction upsertedReaction = reactionRepository.upsert(Reaction
                        .createReaction()
                        .id(ReactionDomainId.of(userId, messageId))
                        .reactionType(type).build());


        if (!existsBefore) {
            Events.publishEvent(new MessageReactionFirstCreatedEvent(userId, type, messageId));
        }

        return upsertedReaction.getMessageId();
    }

    public void deleteReaction(UUID userId, UUID messageId) {
        reactionRepository.deleteByUserIdAndMessageId(userId, messageId);
    }
}
