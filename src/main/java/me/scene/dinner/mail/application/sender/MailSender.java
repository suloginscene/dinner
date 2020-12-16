package me.scene.dinner.mail.application.sender;

import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
public abstract class MailSender {

    protected abstract void doSend(String subject, String to, String message) throws MessagingException;

    public void send(String subject, String to, String message) {
        try {
            doSend(subject, to, message);
        } catch (MessagingException e) {
            throw new RuntimeMessagingException(e.getMessage());
        }
    }

}
