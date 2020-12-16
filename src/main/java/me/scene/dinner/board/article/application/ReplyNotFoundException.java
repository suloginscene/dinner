package me.scene.dinner.board.article.application;

import me.scene.dinner.board.common.BoardNotFoundException;

public class ReplyNotFoundException extends BoardNotFoundException {

    public ReplyNotFoundException(Long id) {
        super(id.toString());
    }

}
