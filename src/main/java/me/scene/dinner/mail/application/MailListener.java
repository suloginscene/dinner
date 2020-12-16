package me.scene.dinner.mail.application;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.command.event.TempAccountCreatedEvent;
import me.scene.dinner.account.application.command.event.TempPasswordIssuedEvent;
import me.scene.dinner.mail.application.sender.MailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailListener {

    @Value("${dinner.url}")
    private String url;

    private final MailSender mailSender;

    private static final String ON_JOIN_TITLE = "[Dinner] Please verify your email address.";
    private static final String ON_JOIN_TEMPLATE = "Verification Link: " + ("%s/verify?email=%s&token=%s");

    private static final String ON_FORGOT_TITLE = "[Dinner] New Random Password.";
    private static final String ON_FORGOT_TEMPLATE = "New password: %s";


    @EventListener
    public void onApplicationEvent(TempAccountCreatedEvent event) {
        String email = event.getEmail();
        String token = event.getVerificationToken();
        String text = String.format(ON_JOIN_TEMPLATE, url, email, token);

        mailSender.send(ON_JOIN_TITLE, email, text);
    }

    @EventListener
    public void onApplicationEvent(TempPasswordIssuedEvent event) {
        String email = event.getEmail();
        String tempRawPassword = event.getTempRawPassword();
        String text = String.format(ON_FORGOT_TEMPLATE, tempRawPassword);

        mailSender.send(ON_FORGOT_TITLE, email, text);
    }

}
