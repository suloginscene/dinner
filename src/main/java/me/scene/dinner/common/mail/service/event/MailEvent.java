package me.scene.dinner.common.mail.service.event;

import lombok.Data;
import me.scene.dinner.common.mail.service.sender.MailMessage;


@Data
public abstract class MailEvent {

    private final MailMessage mailMessage;

}
