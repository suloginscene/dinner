package me.scene.dinner.board.magazine.domain.managed;

import lombok.Data;


@Data
public class MemberAppliedEvent {

    private final Long id;
    private final String title;
    private final String manager;
    private final String name;

    protected MemberAppliedEvent(ManagedMagazine magazine, String memberName) {
        id = magazine.getId();
        title = magazine.getTitle();
        manager = magazine.getOwner().getName();
        name = memberName;
    }

}
