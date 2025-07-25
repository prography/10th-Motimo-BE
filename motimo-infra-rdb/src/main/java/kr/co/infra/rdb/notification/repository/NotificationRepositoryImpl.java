package kr.co.infra.rdb.notification.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.co.domain.common.pagination.CustomPage;
import kr.co.domain.notification.Notification;
import kr.co.domain.notification.NotificationType;
import kr.co.domain.notification.repository.NotificationRepository;
import kr.co.infra.rdb.notification.NotificationEntity;
import kr.co.infra.rdb.notification.QNotificationEntity;
import kr.co.infra.rdb.notification.util.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {
    private final NotificationJpaRepository notificationJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public UUID create(Notification notification) {
        NotificationEntity entity = notificationJpaRepository.save(
                NotificationMapper.toEntity(notification)
        );

        return entity.getId();
    }

    public boolean existsByTodayPoke(UUID senderId, UUID receiverId, UUID groupId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        QNotificationEntity notification = QNotificationEntity.notificationEntity;

        BooleanExpression condition = notification.senderId.eq(senderId)
                .and(notification.receiverId.eq(receiverId))
                .and(notification.referenceId.eq(groupId))
                .and(notification.type.eq(NotificationType.POKE))
                .and(notification.createdAt.between(startOfDay, endOfDay));

        return jpaQueryFactory
                .selectOne()
                .from(notification)
                .where(condition)
                .fetchFirst() != null;
    }

    @Override
    public CustomPage<Notification> findAllByReceiverId(UUID receiverId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<NotificationEntity> pageEntities = notificationJpaRepository.findAllByReceiverId(receiverId, pageable);

        List<Notification> content = pageEntities.getContent().stream()
                .map(NotificationMapper::toDomain)
                .toList();

        return new CustomPage<>(
                content,
                pageEntities.getTotalElements(),
                pageEntities.getTotalPages(),
                page,
                size
        );
    }
}
