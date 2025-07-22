package kr.co.infra.rdb.group.util;

import java.time.LocalDateTime;
import kr.co.domain.group.GroupMember;
import kr.co.infra.rdb.group.entity.GroupMemberEntity;
import kr.co.infra.rdb.group.repository.projection.GroupMemberUserProjection;
import kr.co.infra.rdb.user.entity.UserEntity;

public class GroupMemberMapper {

    public static GroupMember toDomain(GroupMemberEntity entity, UserEntity user) {
        return GroupMember.builder()
                .memberId(entity.getUserId())
                .goalId(entity.getGoalId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileImagePath(user.getProfileImagePath())
                .joinedDate(entity.getJoinedDate())
                .lastOnlineDate(LocalDateTime.now())
                .isNotificationActive(entity.isNotificationActive()).build();
    }

    public static GroupMember toDomain(GroupMemberUserProjection projection) {
        return GroupMember.builder()
                .memberId(projection.getMemberId())
                .goalId(projection.getGoalId())
                .nickname(projection.getNickname())
                .email(projection.getEmail())
                .profileImagePath(projection.getProfileImagePath())
                .joinedDate(projection.getJoinedDate())
                .lastOnlineDate(LocalDateTime.now())
                .isNotificationActive(projection.getIsNotificationActive()).build();
    }
}
