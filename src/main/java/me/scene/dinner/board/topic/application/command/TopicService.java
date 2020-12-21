package me.scene.dinner.board.topic.application.command;

import lombok.RequiredArgsConstructor;
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
        String username = request.getUsername();
        Long magazineId = request.getMagazineId();
        String title = request.getTitle();
        String shortExplanation = request.getShortExplanation();
        String longExplanation = request.getLongExplanation();

        Magazine magazine = magazineRepository.find(magazineId);
        Topic topic = new Topic(magazine, username, title, shortExplanation, longExplanation);
        return repository.save(topic).getId();
    }

    public void update(TopicUpdateRequest request) {
        Long id = request.getId();
        String username = request.getUsername();
        String title = request.getTitle();
        String shortExplanation = request.getShortExplanation();
        String longExplanation = request.getLongExplanation();

        Topic topic = repository.find(id);
        topic.update(username, title, shortExplanation, longExplanation);
    }

    public Long delete(Long id, String current) {
        Topic topic = repository.find(id);
        topic.beforeDelete(current);
        repository.delete(topic);
        return topic.getMagazine().getId();
    }

}