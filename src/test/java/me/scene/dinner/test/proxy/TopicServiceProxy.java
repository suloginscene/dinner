package me.scene.dinner.test.proxy;

import lombok.extern.slf4j.Slf4j;
import me.scene.dinner.board.magazine.application.MagazineService;
import me.scene.dinner.board.topic.application.TopicService;
import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.board.topic.domain.TopicRepository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
public class TopicServiceProxy extends TopicService {

    private final TopicRepository topicRepository;

    public TopicServiceProxy(TopicRepository topicRepository, MagazineService magazineService) {
        super(topicRepository, magazineService);
        this.topicRepository = topicRepository;
    }

    public Topic load(String title) {
        Topic topic = topicRepository.findByTitle(title).orElseThrow();
        log.debug("load: " + topic.getMagazine());
        log.debug("load: " + topic.getArticles());
        return topic;
    }

}
