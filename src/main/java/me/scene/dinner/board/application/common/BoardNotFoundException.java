package me.scene.dinner.board.application.common;

public class BoardNotFoundException extends IllegalArgumentException {

    protected BoardNotFoundException(String message) {
        super(message);
    }

}
