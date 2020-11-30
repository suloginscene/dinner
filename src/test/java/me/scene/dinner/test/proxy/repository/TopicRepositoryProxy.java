package me.scene.dinner.test.proxy.repository;

import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.board.topic.domain.TopicRepository;

import java.util.Optional;

public interface TopicRepositoryProxy extends TopicRepository {

    Optional<Topic> findByTitle(String title);

}
