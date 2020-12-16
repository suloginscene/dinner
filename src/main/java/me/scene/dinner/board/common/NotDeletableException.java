package me.scene.dinner.board.common;

public class NotDeletableException extends IllegalStateException {

    public NotDeletableException(String target) {
        super(target);
    }

}
