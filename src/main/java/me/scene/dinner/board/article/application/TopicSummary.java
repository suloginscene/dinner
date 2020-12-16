package me.scene.dinner.board.article.application;

import lombok.Getter;
import me.scene.dinner.board.topic.domain.Topic;

@Getter
public class TopicSummary {

    private final Long id;

    private final String title;

    public TopicSummary(Topic topic) {
        this.id = topic.getId();
        this.title = topic.getTitle();
    }

}
