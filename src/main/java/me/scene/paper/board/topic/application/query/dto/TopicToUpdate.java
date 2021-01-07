package me.scene.paper.board.topic.application.query.dto;

import lombok.Getter;
import me.scene.paper.board.common.dto.View;
import me.scene.paper.board.topic.domain.model.Topic;


@Getter
public class TopicToUpdate extends View {

    private final String shortExplanation;
    private final String longExplanation;


    public TopicToUpdate(Topic topic) {
        super(topic);

        this.shortExplanation = topic.getShortExplanation();
        this.longExplanation = topic.getLongExplanation();
    }

}
