package me.scene.dinner.account.application.command.exception;

public class VerificationException extends IllegalArgumentException {

    public VerificationException(String token) {
        super(token);
    }

}
