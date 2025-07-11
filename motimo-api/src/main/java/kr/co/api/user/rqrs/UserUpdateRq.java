package kr.co.api.user.rqrs;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import kr.co.domain.user.model.InterestType;

public record UserUpdateRq(

        @Schema(description = "변경할 유저 이름", example = "user name")
        String userName,

        @JsonSetter(nulls = Nulls.AS_EMPTY)
        @Schema(description = "한줄 소개", example = "안녕하세요.")
        String bio,

        @JsonSetter(nulls = Nulls.AS_EMPTY)
        @Schema(description = "유저 관심사 목록", example = "[\"HEALTH\", \"PROGRAMMING\", \"SPORTS\"]")
        Set<InterestType> interests
) {

}
