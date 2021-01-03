package me.scene.paper.integration.utils;

import lombok.RequiredArgsConstructor;
import me.scene.paper.board.topic.application.command.TopicService;
import me.scene.paper.board.topic.application.command.request.TopicCreateRequest;
import me.scene.paper.board.topic.domain.repository.TopicRepository;
import me.scene.paper.integration.utils.aop.LogAround;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TopicTestHelper {

    private final TopicService service;

    private final TopicRepository repository;


    @LogAround
    public Long createTopic(String username, Long magazineId, String title) {
        TopicCreateRequest request = new TopicCreateRequest(username, magazineId, title, "short", "long");
        return service.save(request);
    }

    @LogAround
    public void clearTopics() {
        repository.deleteAll();
    }

}
