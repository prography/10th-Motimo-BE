package kr.co.infra.rdb.group.util;

import kr.co.domain.group.GroupMember;
import kr.co.infra.rdb.group.entity.GroupMemberEntity;
import kr.co.infra.rdb.user.entity.UserEntity;

public class GroupMemberMapper {

    public static GroupMember toDomain(GroupMemberEntity entity, UserEntity user) {
        return GroupMember.builder()
                .memberId(entity.getUserId())
                .goalId(entity.getGoalId())
                .name(user.getNickname())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImagePath())
                .joinedDate(entity.getJoinedDate())
                .isNotificationActive(entity.isNotificationActive()).build();
    }
}
