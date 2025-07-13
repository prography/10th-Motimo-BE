package kr.co.domain.common.event;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginEvent extends Event{
    private UUID userId;

    public UserLoginEvent(UUID userId) {
        super();
        this.userId = userId;
    }
}
