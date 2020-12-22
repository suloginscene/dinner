package me.scene.dinner.board.article.application.query;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.domain.article.model.Like;
import me.scene.dinner.board.article.domain.article.repository.ArticleRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeQueryService {

    private final ArticleRepository repository;

    public boolean doesLike(String username, Long articleId) {
        Set<Like> likes = repository.find(articleId).getLikes();
        return likes.contains(new Like(username));
    }

}
