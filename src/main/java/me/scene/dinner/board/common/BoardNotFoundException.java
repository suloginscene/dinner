package me.scene.dinner.board.common;

public class BoardNotFoundException extends IllegalArgumentException {

    protected BoardNotFoundException(String message) {
        super(message);
    }

}
