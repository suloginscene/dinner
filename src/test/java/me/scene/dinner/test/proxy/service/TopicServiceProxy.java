package me.scene.dinner.test.proxy.service;

import lombok.extern.slf4j.Slf4j;
import me.scene.dinner.board.application.magazine.MagazineService;
import me.scene.dinner.board.application.topic.TopicService;
import me.scene.dinner.board.domain.topic.Topic;
import me.scene.dinner.test.proxy.repository.TopicRepositoryProxy;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
public class TopicServiceProxy extends TopicService {

    private final TopicRepositoryProxy topicRepository;

    public TopicServiceProxy(TopicRepositoryProxy topicRepository, MagazineService magazineService) {
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
