package me.scene.dinner.infra.exception;

// TODO refine usages
public class ForbiddenException extends DinnerException {
    public ForbiddenException(String invader, String target) {
        super(invader + " approached to " + target);
    }
}
