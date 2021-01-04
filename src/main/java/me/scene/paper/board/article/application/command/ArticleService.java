package me.scene.paper.board.article.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.paper.board.article.application.command.request.ArticleCreateRequest;
import me.scene.paper.board.article.application.command.request.ArticleUpdateRequest;
import me.scene.paper.board.article.application.command.request.ReplyCreateRequest;
import me.scene.paper.board.article.application.command.request.ReplyDeleteRequest;
import me.scene.paper.board.article.domain.article.model.Article;
import me.scene.paper.board.article.domain.article.model.Like;
import me.scene.paper.board.article.domain.article.model.Reply;
import me.scene.paper.board.article.domain.article.repository.ArticleRepository;
import me.scene.paper.board.common.domain.model.Point;
import me.scene.paper.board.topic.domain.model.Topic;
import me.scene.paper.board.topic.domain.repository.TopicRepository;
import me.scene.paper.common.notification.event.NotificationEventPublisher;
import me.scene.paper.common.notification.message.NotificationMessageFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository repository;
    private final TopicRepository topicRepository;

    private final TagService tagService;

    private final NotificationMessageFactory messageFactory;
    private final NotificationEventPublisher notification;


    public void read(Long id, String username) {
        Article article = repository.fetchToRate(id);

        if (article.isPublicized()) {
            article.read();
            article.rate(Point.READ);
        } else {
            article.getOwner().identify(username);
        }
    }

    public Long save(ArticleCreateRequest request) {
        Long topicId = request.getTopicId();
        String username = request.getUsername();
        String title = request.getTitle();
        String content = request.getContent();
        boolean publicized = request.isPublicized();

        Topic topic = topicRepository.findFetch(topicId);

        Article article = new Article(topic, username, title, content, publicized);
        Long id = repository.save(article).getId();

        Set<String> tagNames = request.getTagNames();
        tagService.allocate(article, tagNames);

        return id;
    }

    public void update(ArticleUpdateRequest request) {
        Long id = request.getId();
        String username = request.getUsername();
        String title = request.getTitle();
        String content = request.getContent();
        boolean publicized = request.isPublicized();

        Article article = repository.fetchToRate(id);
        article.update(username, title, content, publicized);

        Set<String> tagNames = request.getTagNames();
        tagService.renew(article, tagNames);
    }

    public Long delete(Long id, String current) {
        Article article = repository.fetchToRate(id);
        article.beforeDelete(current);
        repository.delete(article);

        tagService.release(article);

        return article.getTopic().getId();
    }


    public void save(ReplyCreateRequest request) {
        Long articleId = request.getArticleId();
        String username = request.getUsername();
        String content = request.getContent();

        Reply reply = new Reply(username, content);

        Article article = repository.find(articleId);
        List<Reply> replies = article.getReplies();
        replies.add(reply);

        String receiver = article.getOwner().name();
        String message = messageFactory.articleReplied(username, articleId, article.getTitle());
        notification.publish(receiver, message);
    }

    public void delete(ReplyDeleteRequest request) {
        Long articleId = request.getArticleId();
        Long replyId = request.getReplyId();
        String username = request.getUsername();

        List<Reply> replies = repository.find(articleId).getReplies();

        Optional<Reply> optionalReply = replies.stream()
                .filter(reply -> reply.getId().equals(replyId))
                .findAny();

        optionalReply.ifPresent(reply -> {
            reply.getOwner().identify(username);
            replies.remove(reply);
        });
    }


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
