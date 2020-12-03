package me.scene.dinner.board.domain.common;

public class NotDeletableException extends IllegalStateException {

    public NotDeletableException(String target) {
        super(target);
    }

}
