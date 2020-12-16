package me.scene.dinner.mail;

import me.scene.dinner.account.application.command.event.TempAccountCreatedEvent;
import me.scene.dinner.account.application.command.event.TempPasswordIssuedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
public abstract class MailSender {

    @Value("${dinner.url}")
    private String url;

    private static final String ON_JOIN_TITLE = "[Dinner] Please verify your email address.";
    private static final String ON_JOIN_TEMPLATE = "Verification Link: " + ("%s/verify?email=%s&token=%s");

    private static final String ON_FORGOT_TITLE = "[Dinner] New Random Password.";
    private static final String ON_FORGOT_TEMPLATE = "New password: %s";


    protected abstract void send(String subject, String to, String text) throws MessagingException;


    @EventListener
    public void onApplicationEvent(TempAccountCreatedEvent event) {
        String email = event.getEmail();
        String token = event.getVerificationToken();
        String text = String.format(ON_JOIN_TEMPLATE, url, email, token);

        try {
            send(ON_JOIN_TITLE, email, text);
        } catch (MessagingException e) {
            throw new RuntimeMessagingException(e.getMessage());
        }
    }

    @EventListener
    public void onApplicationEvent(TempPasswordIssuedEvent event) {
        String email = event.getEmail();
        String tempRawPassword = event.getTempRawPassword();
        String text = String.format(ON_FORGOT_TEMPLATE, tempRawPassword);

        try {
            send(ON_FORGOT_TITLE, email, text);
        } catch (MessagingException e) {
            throw new RuntimeMessagingException(e.getMessage());
        }
    }

}
