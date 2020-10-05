package me.scene.dinner.infra.exception;

public class VerificationException extends DinnerException {
    public VerificationException(String email) {
        super(email);
    }
}
