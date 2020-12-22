package me.scene.dinner.account.application.command.mail;

import lombok.Data;


@Data
public abstract class MailMessage {

    private final String subject;
    private final String to;
    private final String message;

}
