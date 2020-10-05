package me.scene.dinner.infra.mail;

import javax.mail.MessagingException;

public interface MailSender {
    void send(MailMessage mailMessage) throws MessagingException;
}
