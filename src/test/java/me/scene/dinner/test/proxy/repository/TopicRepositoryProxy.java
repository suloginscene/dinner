package me.scene.dinner.test.proxy.repository;

import me.scene.dinner.board.domain.topic.Topic;
import me.scene.dinner.board.domain.topic.TopicRepository;

import java.util.Optional;

public interface TopicRepositoryProxy extends TopicRepository {

    Optional<Topic> findByTitle(String title);

}
