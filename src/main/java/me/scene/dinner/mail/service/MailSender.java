package me.scene.dinner.mail.service;

import me.scene.dinner.account.domain.account.TempPasswordIssuedEvent;
import me.scene.dinner.account.domain.tempaccount.TempAccountCreatedEvent;
import me.scene.dinner.board.magazine.domain.MemberManagedEvent;
import me.scene.dinner.board.magazine.domain.MemberAppliedEvent;
import me.scene.dinner.board.magazine.domain.MemberQuitEvent;
import me.scene.dinner.mail.infra.RuntimeMessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
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
    private static final String ON_APPLIED_LINK_TEMPLATE = "Add member Link: " + ("%s/magazines/%s/%s");

    private static final String ON_QUIT_TITLE = "[Dinner] Member quit your magazine.";
    private static final String ON_QUIT_TEMPLATE = "%s quit your magazine.";
    private static final String ON_QUIT_LINK_TEMPLATE = "Manage member Link: " + ("%s/magazines/%s/members");

    private static final String ON_MANAGED_ADDED_TITLE_TEMPLATE = "[Dinner] Now you are member of %s.";
    private static final String ON_MANAGED_REMOVED_TITLE_TEMPLATE = "[Dinner] Now you are not member of %s.";
    private static final String ON_MANAGED_LINK_TEMPLATE = "Magazine Link: " + ("%s/magazines/%s");

    abstract protected void send(String subject, String to, String text) throws RuntimeMessagingException;

    @EventListener
    public void onApplicationEvent(TempAccountCreatedEvent event) throws SyncMessagingException {
        String email = event.getEmail();
        String token = event.getVerificationToken();
        String text = String.format(ON_JOIN_TEMPLATE, url, email, token);

        try {
            send(ON_JOIN_TITLE, email, text);
        } catch (RuntimeMessagingException e) {
            throw new SyncMessagingException(e.getMessage());
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
            throw new SyncMessagingException(e.getMessage());
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
            throw new SyncMessagingException(e.getMessage());
        }
    }

    @EventListener @Async
    public void onApplicationEvent(MemberQuitEvent event) throws AsyncMessagingException {
        String email = event.getManagerEmail();
        String member = event.getMember();
        Long magazineId = event.getMagazineId();
        String text = String.format(ON_QUIT_TEMPLATE, member) + ", "
                + String.format(ON_QUIT_LINK_TEMPLATE, url, magazineId);

        try {
            send(ON_QUIT_TITLE, email, text);
        } catch (RuntimeMessagingException e) {
            throw new AsyncMessagingException(e.getMessage());
        }
    }

    @EventListener @Async
    public void onApplicationEvent(MemberManagedEvent event) throws AsyncMessagingException {
        String email = event.getMemberEmail();
        String magazine = event.getMagazineTitle();
        Long magazineId = event.getMagazineId();
        String titleTemplate = event.isRemoval() ? ON_MANAGED_REMOVED_TITLE_TEMPLATE : ON_MANAGED_ADDED_TITLE_TEMPLATE;

        String title = String.format(titleTemplate, magazine);
        String text = String.format(ON_MANAGED_LINK_TEMPLATE, url, magazineId);

        try {
            send(title, email, text);
        } catch (RuntimeMessagingException e) {
            throw new AsyncMessagingException(e.getMessage());
        }
    }

}
