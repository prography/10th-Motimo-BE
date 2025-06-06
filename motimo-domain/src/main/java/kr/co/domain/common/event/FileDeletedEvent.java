package kr.co.domain.common.event;

import lombok.Getter;

@Getter
public class FileDeletedEvent extends Event {

    String filePath;

    public FileDeletedEvent(String filePath) {
        super();
        this.filePath = filePath;
    }
}
