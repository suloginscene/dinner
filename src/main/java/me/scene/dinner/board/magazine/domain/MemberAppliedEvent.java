package me.scene.dinner.board.magazine.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter @EqualsAndHashCode(callSuper = false)
public class MemberAppliedEvent extends ApplicationEvent {

    private final Long magazineId;
    private final String managerEmail;
    private final String applicant;

    public MemberAppliedEvent(Object source, Long magazineId, String managerEmail, String applicant) {
        super(source);
        this.magazineId = magazineId;
        this.managerEmail = managerEmail;
        this.applicant = applicant;
    }

}
