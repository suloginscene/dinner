package me.scene.dinner.board.magazine.domain.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter @EqualsAndHashCode(callSuper = false)
public class MemberAppliedEvent extends ApplicationEvent {

    private final Long magazineId;
    private final String managerEmail;
    private final String applicant;
    private final String applicantEmail;

    public MemberAppliedEvent(Object source, Long magazineId, String managerEmail, String applicant, String applicantEmail) {
        super(source);
        this.magazineId = magazineId;
        this.managerEmail = managerEmail;
        this.applicant = applicant;
        this.applicantEmail = applicantEmail;
    }

}
