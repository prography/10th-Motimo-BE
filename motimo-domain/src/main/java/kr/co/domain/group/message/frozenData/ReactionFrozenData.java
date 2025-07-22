package kr.co.domain.group.message.frozenData;

import kr.co.domain.group.reaction.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReactionFrozenData implements FrozenData {
    private ReactionType reactionType;
}