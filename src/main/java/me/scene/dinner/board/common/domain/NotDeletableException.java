package me.scene.dinner.board.common.domain;

public class NotDeletableException extends IllegalStateException {

    public NotDeletableException() {
        super("Child remains");
    }

}
