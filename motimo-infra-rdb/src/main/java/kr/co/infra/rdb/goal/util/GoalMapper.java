package kr.co.infra.rdb.goal.util;

import kr.co.domain.goal.Goal;
import kr.co.infra.rdb.auth.entity.RefreshTokenEntity;
import kr.co.infra.rdb.goal.entity.GoalEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GoalMapper {

    public static Goal toDomain(GoalEntity entity) {

        return Goal.builder()
                .id(entity.getId())
                .userId()
                .title()
                .dueDate()
                .createdAt()
                .updatedAt()
                .completed()
                .completedAt()
                .subGoals();
    }

    public static GoalEntity toEntity(Goal goal) {

        return RefreshTokenEntity.builder()
                .id(refreshToken.getId())
                .tokenId(refreshToken.getTokenId())
                .userId(refreshToken.getUserId())
                .token(refreshToken.getToken())
                .build();
    }
}
