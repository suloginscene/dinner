package me.scene.dinner.like;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.application.article.ArticleService;
import me.scene.dinner.board.domain.article.Article;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final ArticleService articleService;

    @Transactional
    public void likes(String username, Long articleId) {
        Article article = articleService.like(articleId);
        Likes likes = Likes.create(username, articleId, article.getTitle(), article.getOwner().getOwnerName());
        likesRepository.save(likes);
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
