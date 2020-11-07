package me.scene.dinner.board.topic.utils;

import me.scene.dinner.board.topic.application.TopicService;
import me.scene.dinner.board.topic.domain.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicFactory {

    private final TopicService topicService;

    @Autowired
    public TopicFactory(TopicService topicService) {
        this.topicService = topicService;
    }

    public Topic create(Long magazineId, String manager, String title, String shortExplanation, String longExplanation) {
        Long id = topicService.save(magazineId, manager, title, shortExplanation, longExplanation);
        return topicService.find(id);
    }

}
