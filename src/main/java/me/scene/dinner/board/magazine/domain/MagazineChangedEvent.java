package me.scene.dinner.board.magazine.domain;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MagazineChangedEvent extends ApplicationEvent {

    private final boolean deletion;

    public MagazineChangedEvent(Object source) {
        super(source);
        deletion = false;
    }

    public MagazineChangedEvent(Object source, boolean deletion) {
        super(source);
        this.deletion = deletion;
    }

}
