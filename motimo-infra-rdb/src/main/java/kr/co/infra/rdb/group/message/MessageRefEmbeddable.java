package kr.co.infra.rdb.group.message;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.UUID;
import kr.co.domain.group.message.MessageRefType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageRefEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type")
    private MessageRefType messageRefType;

    @Column(name = "reference_id")
    private UUID referenceId;
}
