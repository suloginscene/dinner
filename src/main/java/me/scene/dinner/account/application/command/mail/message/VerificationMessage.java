package me.scene.dinner.account.application.command.mail.message;

import me.scene.dinner.common.mail.service.sender.MailMessage;


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
