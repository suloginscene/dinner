package me.scene.dinner.board.magazine.application;

import me.scene.dinner.board.common.BoardNotFoundException;

public class MagazineNotFoundException extends BoardNotFoundException {

    public MagazineNotFoundException(Long id) {
        super(id.toString());
    }

}
