package me.scene.dinner.board.topic.application;

import me.scene.dinner.board.magazine.application.MagazineService;
import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.board.topic.domain.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional(readOnly = true)
public class TopicService {

    private final TopicRepository topicRepository;
    private final MagazineService magazineService;

    @Autowired
    public TopicService(TopicRepository topicRepository, MagazineService magazineService) {
        this.topicRepository = topicRepository;
        this.magazineService = magazineService;
    }

    @Transactional
    public Long save(Long magazineId, String manager, String title, String shortExplanation, String longExplanation) {
        Topic topic = Topic.create(magazineService.find(magazineId), manager, title, shortExplanation, longExplanation);
        topic = topicRepository.save(topic);
        return topic.getId();
    }

    public Topic find(Long id) {
        return topicRepository.findById(id).orElseThrow(() -> new TopicNotFoundException(id));
    }

}
