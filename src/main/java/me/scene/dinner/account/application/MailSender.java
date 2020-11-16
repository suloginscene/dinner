package me.scene.dinner.account.application;

import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
public interface MailSender {

    // TODO event publish
    void send(String subject, String to, String text) throws MessagingException;

}
