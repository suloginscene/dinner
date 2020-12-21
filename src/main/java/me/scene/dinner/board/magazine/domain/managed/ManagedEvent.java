package me.scene.dinner.board.magazine.domain.managed;

import lombok.Data;


@Data
public class ManagedEvent {

    public enum Action {ADD, REMOVE}

    private final Long id;
    private final String title;
    private final String name;
    private final Action action;

    public ManagedEvent(ManagedMagazine magazine, String memberName, Action action) {
        id = magazine.getId();
        title = magazine.getTitle();
        name = memberName;
        this.action = action;
    }

}
