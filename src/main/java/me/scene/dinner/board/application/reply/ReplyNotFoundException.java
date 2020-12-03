package me.scene.dinner.board.application.reply;

import me.scene.dinner.board.application.common.BoardNotFoundException;

public class ReplyNotFoundException extends BoardNotFoundException {

    public ReplyNotFoundException(Long id) {
        super(id.toString());
    }

}
