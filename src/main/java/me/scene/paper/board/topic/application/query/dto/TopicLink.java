package me.scene.paper.board.topic.application.query.dto;

import me.scene.paper.board.common.dto.Link;
import me.scene.paper.board.topic.domain.model.Topic;

public class TopicLink extends Link {

    public TopicLink(Topic topic) {
        super(topic);
    }

}
