package me.scene.dinner.board.domain.magazine.event;

import org.springframework.context.ApplicationEvent;

public class MagazineDeletedEvent extends ApplicationEvent {

    public MagazineDeletedEvent(Object source) {
        super(source);
    }

}
