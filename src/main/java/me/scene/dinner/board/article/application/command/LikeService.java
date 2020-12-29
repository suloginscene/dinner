package me.scene.dinner.board.article.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.domain.article.model.Article;
import me.scene.dinner.board.article.domain.article.model.Like;
import me.scene.dinner.board.article.domain.article.repository.ArticleRepository;
import me.scene.dinner.board.common.domain.model.Point;
import me.scene.dinner.common.notification.message.NotificationMessageFactory;
import me.scene.dinner.common.notification.event.NotificationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final ArticleRepository repository;

    private final NotificationMessageFactory messageFactory;
    private final NotificationEventPublisher notification;


    public void like(String reader, Long id) {
        Article article = repository.fetchToRate(id);
        Set<Like> likes = article.getLikes();

        Like like = new Like(reader);
        if (likes.contains(like)) return;

        likes.add(like);
        article.rate(Point.LIKE);

        String receiver = article.getOwner().name();
        String message = messageFactory.articleLiked(reader, article.getId(), article.getTitle());
        notification.publish(receiver, message);
    }

    public void dislike(String username, Long articleId) {
        Article article = repository.fetchToRate(articleId);
        Set<Like> likes = article.getLikes();

        Like like = new Like(username);
        if (!likes.contains(like)) return;

        likes.remove(like);
        article.rate(-Point.LIKE);
    }

}
