package me.scene.dinner.board.magazine.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter @EqualsAndHashCode(callSuper = false)
public class MemberQuitEvent extends ApplicationEvent {

    private final Long magazineId;
    private final String managerEmail;
    private final String member;

    public MemberQuitEvent(Object source, Long magazineId, String managerEmail, String member) {
        super(source);
        this.magazineId = magazineId;
        this.managerEmail = managerEmail;
        this.member = member;
    }

}
