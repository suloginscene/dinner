package me.scene.dinner.infra.mail;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class MailMessage {
    private String subject;
    private String to;
    private String text;
}
