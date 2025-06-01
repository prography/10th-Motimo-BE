package kr.co.domain.common.event;

import lombok.Getter;

@Getter
public class ImageDeletedEvent extends Event {

    String imageName;

    public ImageDeletedEvent(String imageName) {
        super();
        this.imageName = imageName;
    }
}
