package kr.co.infra.rdb.auth.entity;

import jakarta.persistence.*;
import kr.co.infra.rdb.common.uuid.GeneratedUuidV7Value;
import lombok.*;
import org.hibernate.annotations.SoftDelete;

import java.util.UUID;

@Entity
@Table(name = "refresh_token")
@SoftDelete(columnName = "is_deleted")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RefreshTokenEntity {
    @Id
    @GeneratedUuidV7Value
    private UUID id;

    @Column(name = "token_id", nullable = false, unique = true)
    private UUID tokenId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "token", nullable = false, length = 512)
    private String token;
}
