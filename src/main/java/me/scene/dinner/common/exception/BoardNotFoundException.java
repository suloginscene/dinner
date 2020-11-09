package me.scene.dinner.common.exception;

public class BoardNotFoundException extends IllegalArgumentException {

    public BoardNotFoundException(String message) {
        super(message);
    }

}
