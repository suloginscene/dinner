package me.scene.paper.service.board.topic.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.paper.service.board.magazine.domain.magazine.model.Magazine;
import me.scene.paper.service.board.magazine.domain.magazine.repository.MagazineRepository;
import me.scene.paper.service.board.topic.application.command.request.TopicCreateRequest;
import me.scene.paper.service.board.topic.application.command.request.TopicUpdateRequest;
import me.scene.paper.service.board.topic.domain.model.Topic;
import me.scene.paper.service.board.topic.domain.repository.TopicRepository;
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
