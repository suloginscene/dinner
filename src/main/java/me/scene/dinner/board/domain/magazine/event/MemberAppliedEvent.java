package me.scene.dinner.board.domain.magazine.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter @EqualsAndHashCode(callSuper = false)
public class MemberAppliedEvent extends ApplicationEvent {

    private final Long magazineId;
    private final String magazineTitle;
    private final String manager;
    private final String applicant;

    public MemberAppliedEvent(Object source, Long magazineId, String magazineTitle, String manager, String applicant) {
        super(source);
        this.magazineId = magazineId;
        this.magazineTitle = magazineTitle;
        this.manager = manager;
        this.applicant = applicant;
    }

}
