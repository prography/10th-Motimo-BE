package kr.co.infra.rdb.group.message;

import java.util.List;
import kr.co.domain.group.message.GroupMessage;
import kr.co.domain.group.reaction.Reaction;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GroupMessageMapper {

    public static GroupMessageEntity toEntity(GroupMessage groupMessage) {
        return new GroupMessageEntity(
                groupMessage.getId(),
                groupMessage.getGroupId(),
                groupMessage.getUserId(),
                groupMessage.getMessageType(),
                MessageReferenceEmbeddable.from(groupMessage.getMessageReference()),
                groupMessage.getSendAt()
        );
    }

    public static GroupMessage toDomain(GroupMessageEntity groupMessageEntity) {
        return GroupMessage.builder()
                .id(groupMessageEntity.getId())
                .groupId(groupMessageEntity.getGroupId())
                .userId(groupMessageEntity.getUserId())
                .messageType(groupMessageEntity.getType())
                .messageReference(
                        groupMessageEntity.getMessageReference() != null
                                ? groupMessageEntity.getMessageReference().toDomain() : null)
                .sendAt(groupMessageEntity.getSendAt())
                .build();
    }

    public static GroupMessage toDomainWithReactions(GroupMessageEntity groupMessageEntity,
            List<Reaction> reactions) {
        return GroupMessage.builder()
                .id(groupMessageEntity.getId())
                .groupId(groupMessageEntity.getGroupId())
                .userId(groupMessageEntity.getUserId())
                .messageType(groupMessageEntity.getType())
                .messageReference(
                        groupMessageEntity.getMessageReference() != null
                                ? groupMessageEntity.getMessageReference().toDomain() : null)
                .reactions(reactions)
                .sendAt(groupMessageEntity.getSendAt())
                .build();
    }

}
