package me.scene.dinner.account.application.command.mail.event;

import me.scene.dinner.common.mail.service.sender.MailMessage;
import me.scene.dinner.common.mail.service.event.MailEvent;

public class AccountMailEvent extends MailEvent {

    public AccountMailEvent(MailMessage mailMessage) {
        super(mailMessage);
    }

}
