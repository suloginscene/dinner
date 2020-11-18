package me.scene.dinner.board.magazine.domain;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MagazineChangedEvent extends ApplicationEvent {

    private final boolean deletion;

    public MagazineChangedEvent(Magazine magazine) {
        super(magazine);
        deletion = false;
    }

    public MagazineChangedEvent(Magazine magazine, boolean deletion) {
        super(magazine);
        this.deletion = deletion;
    }

    @Override
    public Magazine getSource() {
        return (Magazine) super.getSource();
    }

}
