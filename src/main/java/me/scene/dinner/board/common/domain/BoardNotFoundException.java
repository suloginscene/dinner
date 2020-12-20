package me.scene.dinner.board.common.domain;


public class BoardNotFoundException extends IllegalArgumentException {

    public BoardNotFoundException(Long id) {
        super(id.toString());
    }

}
