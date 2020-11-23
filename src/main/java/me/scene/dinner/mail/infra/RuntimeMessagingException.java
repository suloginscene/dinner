package me.scene.dinner.mail.infra;

public class RuntimeMessagingException extends RuntimeException {

    private static final String TEMPLATE = "subject: %s, to: %s";

    public RuntimeMessagingException(String subject, String to) {
        super(String.format(TEMPLATE, subject, to));
    }

    protected RuntimeMessagingException(String message) {
        super(message);
    }

}
