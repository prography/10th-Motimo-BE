package kr.co.infra.rdb.group;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import kr.co.infra.rdb.common.uuid.GeneratedUuidV7Value;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "groups")
@SoftDelete(columnName = "is_deleted")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupEntity {
    @Id
    @GeneratedUuidV7Value
    private UUID id;

//    private String name;

}
