package me.scene.dinner.like;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.application.ArticleService;
import me.scene.dinner.board.article.domain.Article;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final ArticleService articleService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void likes(String username, Long articleId) {
        Article article = articleService.like(articleId);
        Likes likes = Likes.create(username, articleId);
        likesRepository.save(likes);
        eventPublisher.publishEvent(new LikedEvent(article.getWriter(), username, article.getTitle()));
    }

    @Transactional
    public void dislike(String username, Long articleId) {
        articleService.dislike(articleId);
        Likes likes = likesRepository.findByUsernameAndArticleId(username, articleId);
        if (likes == null) throw new IllegalStateException("Likes should not be null");
        likesRepository.delete(likes);
    }

    public boolean doesLike(String username, Long articleId) {
        return likesRepository.existsByUsernameAndArticleId(username, articleId);
    }

}
