package me.scene.paper.board.topic.application.query;

import lombok.RequiredArgsConstructor;
import me.scene.paper.board.topic.application.query.dto.TopicLink;
import me.scene.paper.board.topic.application.query.dto.TopicView;
import me.scene.paper.board.topic.domain.model.Topic;
import me.scene.paper.board.topic.domain.repository.TopicRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TopicQueryService {

    private final TopicRepository repository;


    public TopicView find(Long id) {
        Topic topic = repository.findFetch(id);
        return new TopicView(topic);
    }

    public List<TopicLink> linksOfMagazine(Long magazineId) {
        List<Topic> topics = repository.findByMagazineId(magazineId);
        return topics.stream()
                .map(TopicLink::new)
                .collect(toList());
    }

}
