package me.scene.dinner.board.article.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.application.command.request.ArticleCreateRequest;
import me.scene.dinner.board.article.application.command.request.ArticleUpdateRequest;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.article.domain.ArticleRepository;
import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.board.topic.domain.TopicRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository repository;
    private final TopicRepository topicRepository;

    private final ApplicationEventPublisher eventPublisher;


    public Long save(ArticleCreateRequest request) {
        Long topicId = request.getTopicId();
        String username = request.getUsername();
        String title = request.getTitle();
        String content = request.getContent();
        boolean publicized = request.isPublicized();

        Topic topic = topicRepository.find(topicId);

        Article article = new Article(topic, username, title, content, publicized);
        return repository.save(article).getId();
    }

    public void update(ArticleUpdateRequest request) {
        Long id = request.getId();
        String username = request.getUsername();
        String title = request.getTitle();
        String content = request.getContent();
        boolean publicized = request.isPublicized();

        Article article = repository.find(id);
        article.update(username, title, content, publicized);
    }

    public Long delete(Long id, String current) {
        Article article = repository.find(id);
        article.beforeDelete(current);
        repository.delete(article);
        return article.getTopic().getId();
    }


    public void publishTaggedEvent(Long id, Set<String> tagNames) {
        Article article = repository.find(id);
        ArticleTaggedEvent event = new ArticleTaggedEvent(article, tagNames);
        eventPublisher.publishEvent(event);
    }

}
