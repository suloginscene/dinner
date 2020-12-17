package me.scene.dinner.board.common;

public class BoardNotFoundException extends IllegalArgumentException {

    public BoardNotFoundException(Long id) {
        super(id.toString());
    }

}
