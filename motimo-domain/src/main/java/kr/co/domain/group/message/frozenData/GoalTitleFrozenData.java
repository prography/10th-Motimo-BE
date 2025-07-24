package kr.co.domain.group.message.frozenData;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GoalTitleFrozenData implements FrozenData {

    @JsonProperty("title")
    private String title;
}