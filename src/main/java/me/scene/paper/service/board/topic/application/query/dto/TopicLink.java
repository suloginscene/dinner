package me.scene.paper.service.board.topic.application.query.dto;

import me.scene.paper.service.board.common.dto.Link;
import me.scene.paper.service.board.topic.domain.model.Topic;

public class TopicLink extends Link {

    public TopicLink(Topic topic) {
        super(topic);
    }

}
