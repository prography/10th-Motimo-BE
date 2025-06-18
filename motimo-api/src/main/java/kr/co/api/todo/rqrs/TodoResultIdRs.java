package kr.co.api.todo.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record TodoResultIdRs(
        @Schema(description = "투두 결과 아이디")
        UUID todoResultId
) {

}
