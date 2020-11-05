package me.scene.dinner.board.topic.application;

import me.scene.dinner.board.common.BoardNotFoundException;

public class TopicNotFoundException extends BoardNotFoundException {

    public TopicNotFoundException(Long id) {
        super(id.toString());
    }

}
