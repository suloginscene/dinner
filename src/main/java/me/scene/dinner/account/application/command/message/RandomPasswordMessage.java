package me.scene.dinner.account.application.command.message;

import me.scene.dinner.account.application.command.mail.MailMessage;

public class RandomPasswordMessage extends MailMessage {

    private static final String TITLE = "[Dinner] New Random Password.";
    private static final String MESSAGE = "New password: %s";

    public RandomPasswordMessage(String to, String rawPassword) {
        super(
                TITLE,
                to,
                String.format(MESSAGE, rawPassword)
        );
    }

}
