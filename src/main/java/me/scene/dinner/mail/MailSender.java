package me.scene.dinner.mail;

import me.scene.dinner.account.domain.account.TempPasswordIssuedEvent;
import me.scene.dinner.account.domain.tempaccount.TempAccountCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
public abstract class MailSender {

    @Value("${dinner.url}")
    private String url;

    abstract protected void send(String subject, String to, String text) throws MessagingException;

    @EventListener
    public void onApplicationEvent(TempAccountCreatedEvent event) throws MessagingException {
        send("[Dinner] Please verify your email address.", event.getEmail(),
                "Verification Link: " + (url + "/verify?email=" + event.getEmail() + "&token=" + event.getVerificationToken()));
    }

    @EventListener
    public void onApplicationEvent(TempPasswordIssuedEvent event) throws MessagingException {
        send("[Dinner] New Random Password.", event.getEmail(), "New password: " + event.getTempRawPassword());
    }

}
