package me.scene.dinner.common.exception;

public class NotDeletableException extends IllegalStateException {

    public NotDeletableException(String target) {
        super(target);
    }

}
