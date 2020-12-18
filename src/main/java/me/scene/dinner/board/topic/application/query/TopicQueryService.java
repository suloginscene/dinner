package me.scene.dinner.board.topic.application.query;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.common.BoardNotFoundException;
import me.scene.dinner.board.topic.application.query.dto.TopicSimpleDto;
import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.board.topic.domain.TopicRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TopicQueryService {

    private final TopicRepository topicRepository;


    public TopicSimpleDto findById(Long id) {
        Topic topic = find(id);
        return new TopicSimpleDto(topic);
    }


    // private ---------------------------------------------------------------------------------------------------------

    private Topic find(Long id) {
        return topicRepository.findById(id).orElseThrow(() -> new BoardNotFoundException(id));
    }

}
