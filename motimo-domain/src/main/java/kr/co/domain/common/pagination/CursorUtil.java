package kr.co.domain.common.pagination;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CursorUtil {

    public String createCursor(UUID id, LocalDateTime sendAt) {
        // UUID + 타임스탬프를 Base64로 인코딩
        String cursorData = id.toString() + "|" + sendAt.toString();
        return Base64.getEncoder().encodeToString(cursorData.getBytes());
    }

    public CustomCursor parseCursor(String cursor) {
        if (cursor == null || cursor.isEmpty()) {
            return null;
        }

        try {
            String decoded = new String(Base64.getDecoder().decode(cursor));
            String[] parts = decoded.split("\\|");
            if (parts.length != 2) {
                // todo: custom exception
                throw new IllegalArgumentException("유효하지 않은 cursor 형식입니다.");
            }
            return new CustomCursor(
                    UUID.fromString(parts[0]),
                    LocalDateTime.parse(parts[1])
            );
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException |
                 DateTimeParseException e) {
            throw new IllegalArgumentException("유효하지 않은 cursor 입니다: " + cursor, e);
        }
    }
}
