package me.scene.dinner.board.topic.application.query.dto;

import me.scene.dinner.board.common.dto.Link;
import me.scene.dinner.board.topic.domain.model.Topic;

public class TopicLink extends Link {

    public TopicLink(Topic topic) {
        super(topic);
    }

}
