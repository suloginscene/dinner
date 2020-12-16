package me.scene.dinner.board.article.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.article.domain.ArticleRepository;
import me.scene.dinner.board.article.domain.Reply;
import me.scene.dinner.board.common.Owner;
import me.scene.dinner.board.topic.application.TopicService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service @Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TopicService topicService;
    private final ApplicationEventPublisher eventPublisher;

    public Article find(Long id) {
        return articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id));
    }

    @Transactional
    public Long save(Long topicId, String writer, String title, String content, boolean publicized, Set<String> tagNames) {
        Article article = new Article(topicService.find(topicId), writer, title, content, publicized);
        Long id = articleRepository.save(article).getId();
        // TODO segregate save and tag
        tagNames.forEach(t -> eventPublisher.publishEvent(new ArticleTaggedEvent(article, t)));
        return id;
    }

    @Transactional
    public ArticleSimpleDto read(Long id, String current) {
        Article article = find(id);
        if (article.isPublicized()) article.read();
        else article.getOwner().identify(current);
        return extractDto(article);
    }

    public ArticleSimpleDto findToUpdate(Long id, String current) {
        Article article = find(id);
        article.getOwner().identify(current);
        return extractDto(article);
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

    public List<ArticleSimpleDto> findPublicByWriter(String username) {
        List<Article> articles = articleRepository.findByOwnerAndPublicizedOrderByRatingDesc(new Owner(username), true);
        return articles.stream().map(this::extractDto).collect(Collectors.toList());
    }

    public List<ArticleSimpleDto> findPrivateByWriter(String username) {
        List<Article> articles = articleRepository.findByOwnerAndPublicizedOrderByCreatedAtAsc(new Owner(username), false);
        return articles.stream().map(this::extractDto).collect(Collectors.toList());
    }

    private ArticleSimpleDto extractDto(Article a) {
        // TODO fetch
        List<Reply> replies = a.getReplies();
        log.info("load: {}", replies);

        return new ArticleSimpleDto(a.getId(), a.getOwner().getOwnerName(), a.getTitle(), a.getContent(), a.isPublicized(),
                a.getCreatedAt(), a.getRead(), a.getLikes(), new TopicSummary(a.getTopic()), replies);
    }

}
