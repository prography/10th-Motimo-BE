package kr.co.infra.rdb.todo.entity;

import jakarta.persistence.*;
import kr.co.infra.rdb.common.uuid.GeneratedUuidV7Value;
import lombok.*;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "todo")
@SoftDelete(columnName = "is_deleted")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TodoEntity {

    @Id
    @GeneratedUuidV7Value
    @Column(name = "id")
    private UUID id;

    @Column(name = "sub_goal_id")
    private UUID subGoalId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "date", nullable = false)
    @Builder.Default
    private LocalDate date = LocalDate.now();

    @Column(name = "completed")
    private boolean completed;

    @Embedded
    private TodoResultEmbeddable result;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
