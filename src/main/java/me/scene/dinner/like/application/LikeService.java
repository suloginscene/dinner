package me.scene.dinner.like.application;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.article.domain.ArticleRepository;
import me.scene.dinner.like.domain.Like;
import me.scene.dinner.like.domain.LikeRepository;
import me.scene.dinner.like.domain.LikedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository repository;
    private final ArticleRepository articleRepository;

    private final ApplicationEventPublisher publisher;


    @Transactional(readOnly = true)
    public boolean doesLike(String username, Long articleId) {
        return repository.existsByUsernameAndArticleId(username, articleId);
    }


    public void like(String username, Long articleId) {
        Article article = articleRepository.find(articleId);
        article.like();

        Like like = new Like(username, article);
        repository.save(like);

        LikedEvent event = like.createEvent();
        publisher.publishEvent(event);
    }

    public void dislike(String username, Long articleId) {
        Article article = articleRepository.find(articleId);
        article.dislike();

        Optional<Like> optionalLike = repository.findByUsernameAndArticleId(username, articleId);
        optionalLike.ifPresent(repository::delete);
    }

}
