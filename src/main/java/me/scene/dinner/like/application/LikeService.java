package me.scene.dinner.like.application;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.application.ArticleService;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.like.domain.Like;
import me.scene.dinner.like.domain.LikeRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository repository;

    private final ArticleService articleService;
    private final ApplicationEventPublisher publisher;


    public boolean doesLike(String username, Long articleId) {
        return repository.existsByUsernameAndArticleId(username, articleId);
    }

    @Transactional
    public void like(String username, Long articleId) {
        Article article = articleService.like(articleId);
        Like like = new Like(username, articleId, article.getOwner().getOwnerName(), article.getTitle());
        repository.save(like);
        publisher.publishEvent(like.createEvent());
    }

    @Transactional
    public void dislike(String username, Long articleId) {
        articleService.dislike(articleId);
        repository.findByUsernameAndArticleId(username, articleId).ifPresent(repository::delete);
    }

}
