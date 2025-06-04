package kr.co.infra.rdb.todo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.domain.todo.Emotion;
import kr.co.infra.rdb.common.uuid.GeneratedUuidV7Value;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "todo_result")
@SoftDelete(columnName = "is_deleted")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TodoResultEntity {

    @Id
    @GeneratedUuidV7Value
    @Column(name = "id")
    private UUID id;

    @Column(name = "todo_id", nullable = false)
    private UUID todoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "emotion")
    private Emotion emotion;

    @Column(name = "content")
    private String content;

    @Column(name = "file_url")
    private String fileUrl;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
