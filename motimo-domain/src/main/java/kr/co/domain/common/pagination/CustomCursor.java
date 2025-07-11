package kr.co.domain.common.pagination;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomCursor(UUID id, LocalDateTime dateTime) {

}
