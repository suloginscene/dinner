package me.scene.dinner.board.topic.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.common.BoardNotFoundException;
import me.scene.dinner.board.magazine.domain.common.Magazine;
import me.scene.dinner.board.magazine.domain.common.MagazineRepository;
import me.scene.dinner.board.topic.application.command.request.TopicCreateRequest;
import me.scene.dinner.board.topic.application.command.request.TopicUpdateRequest;
import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.board.topic.domain.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository repository;
    private final MagazineRepository magazineRepository;


    public Long save(TopicCreateRequest request) {
        Magazine magazine = findMagazine(request.getMagazineId());
        Topic topic = createTopic(magazine, request);
        return repository.save(topic).getId();
    }

    public void update(Long id, TopicUpdateRequest request) {
        Topic topic = find(id);
        updateTopic(topic, request);
    }

    public Long delete(Long id, String current) {
        Topic topic = find(id);
        topic.beforeDelete(current);
        repository.delete(topic);
        return topic.getMagazine().getId();
    }


    // private ---------------------------------------------------------------------------------------------------------

    private Topic find(Long id) {
        return repository.findById(id).orElseThrow(() -> new BoardNotFoundException(id));
    }

    private Magazine findMagazine(Long id) {
        return magazineRepository.findById(id).orElseThrow(() -> new BoardNotFoundException(id));
    }


    private Topic createTopic(Magazine magazine, TopicCreateRequest r) {
        return new Topic(magazine, r.getOwnerName(), r.getTitle(), r.getShortExplanation(), r.getLongExplanation());
    }

    private void updateTopic(Topic topic, TopicUpdateRequest r) {
        topic.update(r.getCurrentUsername(), r.getTitle(), r.getShortExplanation(), r.getLongExplanation());
    }

}
