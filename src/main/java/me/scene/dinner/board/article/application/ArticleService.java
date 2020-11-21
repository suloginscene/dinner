package me.scene.dinner.board.article.application;

import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.article.domain.ArticleRepository;
import me.scene.dinner.board.topic.application.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TopicService topicService;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, TopicService topicService) {
        this.articleRepository = articleRepository;
        this.topicService = topicService;
    }

    public Article find(Long id) {
        return articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id));
    }

    @Transactional
    public Long save(Long topicId, String writer, String title, String content) {
        Article article = Article.create(topicService.find(topicId), writer, title, content);
        return articleRepository.save(article).getId();
    }

    @Transactional
    public void publish(Long id, String current) {
        Article article = find(id);
        article.publish(current);
        publishEvent(article);
    }

    @Transactional
    public void update(Long id, String current, String title, String content) {
        Article article = find(id);
        article.update(current, title, content);
    }

    @Transactional
    public Long delete(Long id, String current) {
        Article article = find(id);
        article.beforeDelete(current);
        articleRepository.delete(article);
        return article.getTopic().getId();
    }

    private void publishEvent(Article article) {
        articleRepository.save(article);
    }

}
