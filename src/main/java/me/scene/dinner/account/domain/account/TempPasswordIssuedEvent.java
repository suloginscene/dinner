package me.scene.dinner.account.domain.account;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TempPasswordIssuedEvent extends ApplicationEvent {

    private final String email;
    private final String tempRawPassword;

    public TempPasswordIssuedEvent(Object source, String email, String tempRawPassword) {
        super(source);
        this.email = email;
        this.tempRawPassword = tempRawPassword;
    }

}
