package me.scene.dinner.account.domain.tempaccount;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter @EqualsAndHashCode(callSuper = false)
public class TempAccountCreatedEvent extends ApplicationEvent {

    private final String email;
    private final String verificationToken;

    public TempAccountCreatedEvent(Object source, String email, String verificationToken) {
        super(source);
        this.email = email;
        this.verificationToken = verificationToken;
    }

}
