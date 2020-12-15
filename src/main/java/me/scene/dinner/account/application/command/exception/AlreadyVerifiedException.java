package me.scene.dinner.account.application.command.exception;

public class AlreadyVerifiedException extends IllegalStateException {

    public AlreadyVerifiedException(String email) {
        super(email);
    }

}
