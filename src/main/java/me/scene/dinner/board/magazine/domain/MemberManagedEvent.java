package me.scene.dinner.board.magazine.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter @EqualsAndHashCode(callSuper = false)
public class MemberManagedEvent extends ApplicationEvent {

    private final Long magazineId;
    private final String magazineTitle;
    private final String memberEmail;
    private final boolean removal;

    public MemberManagedEvent(Object source, Long magazineId, String magazineTitle, String memberEmail) {
        super(source);
        this.magazineId = magazineId;
        this.magazineTitle = magazineTitle;
        this.memberEmail = memberEmail;
        this.removal = false;
    }

    public MemberManagedEvent(Object source, Long magazineId, String magazineTitle, String memberEmail, boolean removal) {
        super(source);
        this.magazineId = magazineId;
        this.magazineTitle = magazineTitle;
        this.memberEmail = memberEmail;
        this.removal = removal;
    }

}
