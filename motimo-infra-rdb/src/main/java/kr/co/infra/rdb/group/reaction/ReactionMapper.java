package kr.co.infra.rdb.group.reaction;

import kr.co.domain.group.reaction.Reaction;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ReactionMapper {

    public static Reaction toDomain(ReactionEntity entity) {
        return Reaction.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .messageId(entity.getMessageId())
                .reactionType(entity.getReactionType())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static ReactionEntity toEntity(Reaction reaction) {
        return new ReactionEntity(
                reaction.getId(),
                reaction.getUserId(),
                reaction.getMessageId(),
                reaction.getReactionType()
        );
    }

}
