package me.scene.dinner.account.application;

public class UseridNotFoundException extends IllegalArgumentException {

    public UseridNotFoundException(Long id) {
        super(id.toString());
    }

}
