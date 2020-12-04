package me.scene.dinner.board.application.topic;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.application.magazine.MagazineService;
import me.scene.dinner.board.domain.topic.Topic;
import me.scene.dinner.board.domain.topic.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;

@Service @Transactional(readOnly = true)
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final MagazineService magazineService;

    public Topic find(Long id) {
        return topicRepository.findById(id).orElseThrow(() -> new TopicNotFoundException(id));
    }

    @Transactional
    public Long save(Long magazineId, String manager, String title, String shortExplanation, String longExplanation) {
        Topic topic = Topic.create(magazineService.find(magazineId), manager, title, shortExplanation, longExplanation);
        return topicRepository.save(topic).getId();
    }

    public Topic read(Long id) {
        Topic topic = find(id);
        // Todo
        topic.getArticles().sort(Comparator.comparing(a -> a.getCreatedAt()));
        topic.getArticles().forEach(a -> a.getContent());
        return topic;
    }

    public Topic findToUpdate(Long id, String current) {
        Topic topic = find(id);
        topic.confirmManager(current);
        return topic;
    }

    @Transactional
    public void update(Long id, String current, String title, String shortExplanation, String longExplanation) {
        Topic topic = find(id);
        topic.update(current, title, shortExplanation, longExplanation);
    }

    @Transactional
    public Long delete(Long id, String current) {
        Topic topic = find(id);
        topic.beforeDelete(current);
        topicRepository.delete(topic);
        return topic.getMagazine().getId();
    }

}
