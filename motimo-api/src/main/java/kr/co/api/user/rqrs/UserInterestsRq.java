package kr.co.api.user.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import kr.co.domain.user.model.InterestType;

public record UserInterestsRq(
        @Schema(description = "유저 관심사 목록", example = "[\"HEALTH\", \"PROGRAMMING\", \"SPORTS\"]")
        Set<InterestType> interests) {

}
