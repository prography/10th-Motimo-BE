package kr.co.domain.common.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FileDeletedEvent extends Event {

    String filePath;

    public FileDeletedEvent(String filePath) {
        super();
        this.filePath = filePath;
    }
}
