package me.scene.dinner.board.reply.application;

import me.scene.dinner.board.common.exception.BoardNotFoundException;

public class ReplyNotFoundException extends BoardNotFoundException {

    public ReplyNotFoundException(Long id) {
        super(id.toString());
    }

}
