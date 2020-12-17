package me.scene.dinner.board.article.command.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.article.domain.ArticleRepository;
import me.scene.dinner.board.common.BoardNotFoundException;
import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.board.topic.domain.TopicRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service @Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TopicRepository topicRepository;

    private final ApplicationEventPublisher eventPublisher;


    private Article find(Long id) {
        return articleRepository.findById(id).orElseThrow(() -> new BoardNotFoundException(id));
    }

    @Transactional
    public Long save(Long topicId, String writer, String title, String content, boolean publicized, Set<String> tagNames) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new BoardNotFoundException(topicId));
        Article article = new Article(topic, writer, title, content, publicized);
        Long id = articleRepository.save(article).getId();
        // TODO segregate save and tag
        tagNames.forEach(t -> eventPublisher.publishEvent(new ArticleTaggedEvent(article, t)));
        return id;
    }

    @Transactional
    public Article like(Long id) {
        Article article = find(id);
        article.like();
        return article;
    }

    @Transactional
    public void dislike(Long id) {
        Article article = find(id);
        article.dislike();
    }

    @Transactional
    public void update(Long id, String current, String title, String content, boolean publicized) {
        Article article = find(id);
        article.update(current, title, content, publicized);
    }

    @Transactional
    public Long delete(Long id, String current) {
        Article article = find(id);
        article.beforeDelete(current);
        articleRepository.delete(article);
        return article.getTopic().getId();
    }

}
