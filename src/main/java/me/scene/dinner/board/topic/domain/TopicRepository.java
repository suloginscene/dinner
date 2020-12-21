package me.scene.dinner.board.topic.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TopicRepository extends JpaRepository<Topic, Long> {

    @EntityGraph(attributePaths = {"magazine"})
    Topic findToInjectById(Long id);


    default Topic find(Long id) {
        return findById(id).orElseThrow();
    }

}
