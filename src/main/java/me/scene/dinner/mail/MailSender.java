package me.scene.dinner.mail;

import me.scene.dinner.account.domain.account.TempPasswordIssuedEvent;
import me.scene.dinner.account.domain.tempaccount.TempAccountCreatedEvent;
import me.scene.dinner.board.magazine.domain.MemberAppliedEvent;
import me.scene.dinner.mail.exception.RuntimeMessagingException;
import me.scene.dinner.mail.exception.SyncMessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public abstract class MailSender {

    @Value("${dinner.url}")
    private String url;

    private static final String ON_JOIN_TITLE = "[Dinner] Please verify your email address.";
    private static final String ON_JOIN_TEMPLATE = "Verification Link: " + ("%s/verify?email=%s&token=%s");

    private static final String ON_FORGOT_TITLE = "[Dinner] New Random Password.";
    private static final String ON_FORGOT_TEMPLATE = "New password: %s";

    private static final String ON_APPLIED_TITLE = "[Dinner] New member applied to your magazine.";
    private static final String ON_APPLIED_APPLICANT_TEMPLATE = "Applicant: %s (%s/@%s)";
    private static final String ON_APPLIED_LINK_TEMPLATE = "Add Link: " + ("%s/magazines/%s/%s");

    abstract protected void send(String subject, String to, String text) throws RuntimeMessagingException;

    @EventListener
    public void onApplicationEvent(TempAccountCreatedEvent event) throws SyncMessagingException {
        String email = event.getEmail();
        String token = event.getVerificationToken();
        String text = String.format(ON_JOIN_TEMPLATE, url, email, token);

        try {
            send(ON_JOIN_TITLE, email, text);
        } catch (RuntimeMessagingException e) {
            throw new SyncMessagingException();
        }
    }

    @EventListener
    public void onApplicationEvent(TempPasswordIssuedEvent event) throws SyncMessagingException {
        String email = event.getEmail();
        String tempRawPassword = event.getTempRawPassword();
        String text = String.format(ON_FORGOT_TEMPLATE, tempRawPassword);

        try {
            send(ON_FORGOT_TITLE, email, text);
        } catch (RuntimeMessagingException e) {
            throw new SyncMessagingException();
        }
    }

    @EventListener
    public void onApplicationEvent(MemberAppliedEvent event) throws SyncMessagingException {
        String email = event.getManagerEmail();
        String applicant = event.getApplicant();
        Long magazineId = event.getMagazineId();
        String text = String.format(ON_APPLIED_APPLICANT_TEMPLATE, applicant, url, applicant) + ", "
                + String.format(ON_APPLIED_LINK_TEMPLATE, url, magazineId, applicant);

        try {
            send(ON_APPLIED_TITLE, email, text);
        } catch (RuntimeMessagingException e) {
            throw new SyncMessagingException();
        }
    }

}
