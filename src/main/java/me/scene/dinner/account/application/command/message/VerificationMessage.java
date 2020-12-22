package me.scene.dinner.account.application.command.message;

import me.scene.dinner.account.application.command.mail.MailMessage;


public class VerificationMessage extends MailMessage {

    private static final String SUBJECT = "[Dinner] Please verify your email address.";
    private static final String MESSAGE = "Verification Link: " + "%s/verify?email=%s&token=%s";

    public VerificationMessage(String to, String url, String token) {
        super(
                SUBJECT,
                to,
                String.format(MESSAGE, url, to, token)
        );
    }

}
