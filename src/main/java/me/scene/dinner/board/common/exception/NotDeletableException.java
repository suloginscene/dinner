package me.scene.dinner.board.common.exception;

public class NotDeletableException extends IllegalStateException {

    public NotDeletableException(String target) {
        super(target);
    }

}
