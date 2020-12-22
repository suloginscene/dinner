package me.scene.dinner.board.common.domain.exception;

public class NotDeletableException extends IllegalStateException {

    public NotDeletableException() {
        super("Child remains");
    }

}
