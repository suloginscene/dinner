package me.scene.dinner.board.magazine.domain.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter @EqualsAndHashCode(callSuper = false)
public class MemberManagedEvent extends ApplicationEvent {

    private final Long magazineId;
    private final String magazineTitle;
    private final String member;
    private final boolean removal;

    public MemberManagedEvent(Object source, Long magazineId, String magazineTitle, String member) {
        super(source);
        this.magazineId = magazineId;
        this.magazineTitle = magazineTitle;
        this.member = member;
        this.removal = false;
    }

    public MemberManagedEvent(Object source, Long magazineId, String magazineTitle, String member, boolean removal) {
        super(source);
        this.magazineId = magazineId;
        this.magazineTitle = magazineTitle;
        this.member = member;
        this.removal = removal;
    }

}
