package me.scene.dinner.board.application.article;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.application.topic.TopicService;
import me.scene.dinner.board.domain.article.Article;
import me.scene.dinner.board.domain.article.ArticleRepository;
import me.scene.dinner.board.domain.article.Status;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TopicService topicService;

    public Article find(Long id) {
        return articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id));
    }

    @Transactional
    public Long save(Long topicId, String writer, String title, String content, String status) {
        Article article = Article.create(topicService.find(topicId), writer, title, content, status);
        return articleRepository.save(article).getId();
    }

    @Transactional
    public Article read(Long id, String current) {
        Article article = find(id);
        if (article.isPublic()) article.read();
        else article.confirmWriter(current);
        return article;
    }

    public Article findToUpdate(Long id, String current) {
        Article article = find(id);
        article.confirmWriter(current);
        return article;
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
    public void update(Long id, String current, String title, String content, String status) {
        Article article = find(id);
        article.update(current, title, content, status);
    }

    @Transactional
    public Long delete(Long id, String current) {
        Article article = find(id);
        article.beforeDelete(current);
        articleRepository.delete(article);
        return article.getTopic().getId();
    }

    public List<Article> findPublicByWriter(String username) {
        return articleRepository.findByWriterAndStatusOrderByRatingDesc(username, Status.PUBLIC);
    }

    public List<Article> findPrivateByWriter(String username) {
        return articleRepository.findByWriterAndStatusOrderByCreatedAtAsc(username, Status.PRIVATE);
    }

}
