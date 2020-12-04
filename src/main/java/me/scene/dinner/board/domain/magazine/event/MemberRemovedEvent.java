package me.scene.dinner.board.domain.magazine.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter @EqualsAndHashCode(callSuper = false)
public class MemberRemovedEvent extends ApplicationEvent {

    private final Long magazineId;
    private final String magazineTitle;
    private final String member;

    public MemberRemovedEvent(Object source, Long magazineId, String magazineTitle, String member) {
        super(source);
        this.magazineId = magazineId;
        this.magazineTitle = magazineTitle;
        this.member = member;
    }

}
