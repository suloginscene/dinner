package me.scene.dinner.board.topic.application.query;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.topic.application.query.dto.TopicView;
import me.scene.dinner.board.topic.domain.model.Topic;
import me.scene.dinner.board.topic.domain.repository.TopicRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TopicQueryService {

    private final TopicRepository repository;


    public TopicView find(Long id) {
        Topic topic = repository.find(id);
        return new TopicView(topic);
    }

}
