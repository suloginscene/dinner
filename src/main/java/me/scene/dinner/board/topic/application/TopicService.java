package me.scene.dinner.board.topic.application;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.common.BoardNotFoundException;
import me.scene.dinner.board.magazine.domain.common.Magazine;
import me.scene.dinner.board.magazine.domain.common.MagazineRepository;
import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.board.topic.domain.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional(readOnly = true)
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final MagazineRepository magazineRepository;

    public Topic find(Long id) {
        return topicRepository.findById(id).orElseThrow(() -> new BoardNotFoundException(id));
    }

    @Transactional
    public Long save(Long magazineId, String manager, String title, String shortExplanation, String longExplanation) {
        Magazine magazine = magazineRepository.findById(magazineId).orElseThrow(() -> new BoardNotFoundException(magazineId));
        Topic topic = new Topic(magazine, manager, title, shortExplanation, longExplanation);
        return topicRepository.save(topic).getId();
    }

    public TopicSimpleDto read(Long id) {
        Topic topic = find(id);
        return extractDto(topic);
    }

    public TopicSimpleDto findToUpdate(Long id, String current) {
        Topic topic = find(id);
        topic.getOwner().identify(current);
        return extractDto(topic);
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

    private TopicSimpleDto extractDto(Topic t) {
        return new TopicSimpleDto(t.getId(), t.getOwner().getOwnerName(), t.getTitle(), t.getShortExplanation(), t.getLongExplanation(),
                new MagazineSummary(t.getMagazine()), t.getArticleCount());
    }

}
