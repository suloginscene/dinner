package me.scene.dinner.board.domain.magazine.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter @EqualsAndHashCode(callSuper = false)
public class MemberQuitEvent extends ApplicationEvent {

    private final Long magazineId;
    private final String magazineTitle;
    private final String manager;
    private final String member;

    public MemberQuitEvent(Object source, Long magazineId, String magazineTitle, String manager, String member) {
        super(source);
        this.magazineId = magazineId;
        this.magazineTitle = magazineTitle;
        this.manager = manager;
        this.member = member;
    }

}
