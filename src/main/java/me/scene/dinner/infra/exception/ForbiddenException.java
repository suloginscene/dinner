package me.scene.dinner.infra.exception;

public class ForbiddenException extends DinnerException {
    public ForbiddenException(String invader, String target) {
        super(invader + " approached to " + target);
    }
}
