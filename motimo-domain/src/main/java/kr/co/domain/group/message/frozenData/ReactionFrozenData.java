package kr.co.domain.group.message.frozenData;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.domain.group.reaction.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReactionFrozenData implements FrozenData {
    @JsonProperty("reactionType")
    private ReactionType reactionType;
}