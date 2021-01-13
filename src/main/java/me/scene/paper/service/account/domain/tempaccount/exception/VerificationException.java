package me.scene.paper.service.account.domain.tempaccount.exception;

public class VerificationException extends IllegalArgumentException {

    public VerificationException(String token) {
        super(token);
    }

}
