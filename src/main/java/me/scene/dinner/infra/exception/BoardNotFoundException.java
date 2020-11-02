package me.scene.dinner.infra.exception;

public class BoardNotFoundException extends DinnerException {

    public BoardNotFoundException(String type, String condition, String info) {
        super(type + " not found by " + condition + ": " + info);
    }

}
