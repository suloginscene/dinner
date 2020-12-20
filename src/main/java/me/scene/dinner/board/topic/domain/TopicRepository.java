package me.scene.dinner.board.topic.domain;

import me.scene.dinner.board.common.domain.BoardNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TopicRepository extends JpaRepository<Topic, Long> {

    default Topic find(Long id) {
        return findById(id).orElseThrow(() -> new BoardNotFoundException(id));
    }

}
