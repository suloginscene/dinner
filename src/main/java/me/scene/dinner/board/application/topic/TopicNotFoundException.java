package me.scene.dinner.board.application.topic;

import me.scene.dinner.board.application.common.BoardNotFoundException;

public class TopicNotFoundException extends BoardNotFoundException {

    public TopicNotFoundException(Long id) {
        super(id.toString());
    }

}
