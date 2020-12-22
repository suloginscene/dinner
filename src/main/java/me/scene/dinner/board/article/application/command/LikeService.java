package me.scene.dinner.board.article.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.domain.article.model.Article;
import me.scene.dinner.board.article.domain.article.repository.ArticleRepository;
import me.scene.dinner.board.article.domain.article.model.Like;
import me.scene.dinner.board.article.application.command.event.LikedEvent;
import me.scene.dinner.board.common.domain.model.Point;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final ArticleRepository repository;
    private final ApplicationEventPublisher publisher;


    public void like(String reader, Long id) {
        Article article = repository.findFetch(id);
        Set<Like> likes = article.getLikes();

        Like like = new Like(reader);
        likes.add(like);

        article.rate(Point.LIKE);

        LikedEvent event = new LikedEvent(reader, article);
        publisher.publishEvent(event);
    }

    public void dislike(String username, Long articleId) {
        Article article = repository.findFetch(articleId);
        Set<Like> likes = article.getLikes();

        Like target = new Like(username);
        likes.remove(target);

        article.rate(-Point.LIKE);
    }

}
