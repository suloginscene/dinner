package me.scene.dinner.board.reply.application;

import me.scene.dinner.common.exception.BoardNotFoundException;

public class ReplyNotFoundException extends BoardNotFoundException {

    public ReplyNotFoundException(Long id) {
        super(id.toString());
    }

}
