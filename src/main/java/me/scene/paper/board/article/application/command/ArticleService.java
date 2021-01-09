package me.scene.paper.board.article.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.paper.board.article.application.command.request.ArticleCreateRequest;
import me.scene.paper.board.article.application.command.request.ArticleUpdateRequest;
import me.scene.paper.board.article.application.command.request.ReplyCreateRequest;
import me.scene.paper.board.article.application.command.request.ReplyDeleteRequest;
import me.scene.paper.board.article.domain.article.model.Article;
import me.scene.paper.board.article.domain.article.model.Reply;
import me.scene.paper.board.article.domain.article.repository.ArticleRepository;
import me.scene.paper.board.topic.domain.model.Topic;
import me.scene.paper.board.topic.domain.repository.TopicRepository;
import me.scene.paper.common.notification.event.NotificationEventPublisher;
import me.scene.paper.common.notification.message.NotificationMessageFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        article.read(username);
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


    public void saveReply(ReplyCreateRequest request) {
        Long articleId = request.getArticleId();
        String username = request.getUsername();
        String content = request.getContent();

        Article article = repository.find(articleId);
        article.addReply(new Reply(username, content));

        String receiver = article.getOwnerName();
        String message = messageFactory.articleReplied(username, articleId, article.getTitle());
        notification.publish(receiver, message);
    }

    public void removeReply(ReplyDeleteRequest request) {
        Long articleId = request.getArticleId();
        Long replyId = request.getReplyId();
        String username = request.getUsername();

        Article article = repository.find(articleId);
        article.removeReply(username, replyId);
    }


    public void like(String reader, Long id) {
        Article article = repository.fetchToRate(id);
        article.like(reader);

        String receiver = article.getOwnerName();
        String message = messageFactory.articleLiked(reader, article.getId(), article.getTitle());
        notification.publish(receiver, message);
    }

    public void dislike(String username, Long articleId) {
        Article article = repository.fetchToRate(articleId);
        article.dislike(username);
    }

}
