package me.scene.dinner.common.exception;

public class BoardNotFoundException extends IllegalArgumentException {

    protected BoardNotFoundException(String message) {
        super(message);
    }

}
