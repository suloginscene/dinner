package me.scene.dinner.account.domain;

public class VerificationException extends IllegalArgumentException {
    public VerificationException(String token) {
        super(token);
    }
}
