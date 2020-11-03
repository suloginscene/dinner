package me.scene.dinner.infra.exception;

public class ForbiddenException extends DinnerException {
    public ForbiddenException(String principal) {
        super(principal + " approached");
    }
}
