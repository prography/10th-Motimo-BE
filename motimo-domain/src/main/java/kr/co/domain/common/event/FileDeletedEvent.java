package kr.co.domain.common.event;

import lombok.Getter;

@Getter
public class FileDeletedEvent extends Event {

    String fileUrl;

    public FileDeletedEvent(String fileUrl) {
        super();
        this.fileUrl = fileUrl;
    }
}
