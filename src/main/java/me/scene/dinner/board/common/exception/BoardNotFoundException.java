package me.scene.dinner.board.common.exception;

public class BoardNotFoundException extends IllegalArgumentException {

    protected BoardNotFoundException(String message) {
        super(message);
    }

}
