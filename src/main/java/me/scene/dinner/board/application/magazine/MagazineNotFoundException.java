package me.scene.dinner.board.application.magazine;

import me.scene.dinner.board.application.common.BoardNotFoundException;

public class MagazineNotFoundException extends BoardNotFoundException {

    public MagazineNotFoundException(Long id) {
        super(id.toString());
    }

}
