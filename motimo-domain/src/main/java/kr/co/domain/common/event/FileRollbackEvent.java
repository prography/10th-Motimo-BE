package kr.co.domain.common.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FileRollbackEvent extends Event {

    private String filePath;

    public FileRollbackEvent(String filePath) {
        super();
        this.filePath = filePath;
    }
}
