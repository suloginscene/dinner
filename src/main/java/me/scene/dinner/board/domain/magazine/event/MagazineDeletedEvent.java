package me.scene.dinner.board.domain.magazine.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MagazineDeletedEvent extends ApplicationEvent {

    private final Long id;

    public MagazineDeletedEvent(Object source, Long id) {
        super(source);
        this.id = id;
    }

}
