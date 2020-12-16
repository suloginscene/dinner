package me.scene.dinner.test.factory;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.topic.application.TopicService;
import me.scene.dinner.board.topic.domain.Topic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TopicFactory {

    private final TopicService topicService;

    public Topic create(Long magazineId, String manager, String title, String shortExplanation, String longExplanation) {
        Long id = topicService.save(magazineId, manager, title, shortExplanation, longExplanation);
        return topicService.find(id);
    }

}
