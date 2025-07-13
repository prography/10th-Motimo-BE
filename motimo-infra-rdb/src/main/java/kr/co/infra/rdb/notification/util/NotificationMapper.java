package kr.co.infra.rdb.notification.util;

import kr.co.domain.notification.Notification;
import kr.co.infra.rdb.notification.NotificationEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NotificationMapper {

    public Notification toDomain(NotificationEntity entity) {
        return Notification.builder()
                .id(entity.getId())
                .senderId(entity.getSenderId())
                .receiverId(entity.getReceiverId())
                .referenceId(entity.getReferenceId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .type(entity.getType())
                .build();
    }

    public NotificationEntity toEntity(Notification notification) {
        return new NotificationEntity(
                notification.getSenderId(),
                notification.getReceiverId(),
                notification.getTitle(),
                notification.getContent(),
                notification.getType(),
                notification.getReferenceId()
        );
    }
}
