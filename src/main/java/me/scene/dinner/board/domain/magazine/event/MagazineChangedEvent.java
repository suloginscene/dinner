package me.scene.dinner.board.domain.magazine.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MagazineChangedEvent extends ApplicationEvent {

    private final Long id;

    public MagazineChangedEvent(Object source, Long id) {
        super(source);
        this.id = id;
    }

}
