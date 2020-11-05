package me.scene.dinner.account.application;

public class AlreadyVerifiedException extends IllegalStateException {

    public AlreadyVerifiedException(String email) {
        super(email);
    }

}
