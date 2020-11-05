package me.scene.dinner.board.common;

public class BoardNotFoundException extends IllegalArgumentException {

    public BoardNotFoundException(String message) {
        super(message);
    }

}
