package me.scene.paper.service.board.common.domain.exception;

public class NotDeletableException extends IllegalStateException {

    public NotDeletableException() {
        super("Child remains");
    }

}
