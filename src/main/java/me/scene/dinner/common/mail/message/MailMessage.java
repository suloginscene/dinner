package me.scene.dinner.common.mail.message;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode
public class MailMessage {

    private final String subject;
    private final String to;
    private final String message;

}
