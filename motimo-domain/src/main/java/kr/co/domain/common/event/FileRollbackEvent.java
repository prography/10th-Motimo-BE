package kr.co.domain.common.event;

import lombok.Getter;

@Getter
public class FileRollbackEvent extends Event {

    String filePath;

    public FileRollbackEvent(String filePath) {
        super();
        this.filePath = filePath;
    }
}
