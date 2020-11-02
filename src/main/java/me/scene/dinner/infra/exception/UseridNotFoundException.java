package me.scene.dinner.infra.exception;

public class UseridNotFoundException extends DinnerException {

    public UseridNotFoundException(Long id) {
        super("user not found by id: " + id);
    }

}
