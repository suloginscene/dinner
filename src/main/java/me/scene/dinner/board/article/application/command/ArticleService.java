package me.scene.dinner.board.article.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.application.command.request.ArticleCreateRequest;
import me.scene.dinner.board.article.application.command.request.ArticleUpdateRequest;
import me.scene.dinner.board.article.domain.article.model.Article;
import me.scene.dinner.board.article.domain.article.model.ArticleTag;
import me.scene.dinner.board.article.domain.article.repository.ArticleRepository;
import me.scene.dinner.board.article.domain.tag.model.Tag;
import me.scene.dinner.board.article.domain.tag.repository.TagRepository;
import me.scene.dinner.board.common.domain.model.Point;
import me.scene.dinner.board.topic.domain.model.Topic;
import me.scene.dinner.board.topic.domain.repository.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository repository;
    private final TopicRepository topicRepository;
    private final TagRepository tagRepository;


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
        renewTags(article, tagNames);

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
    }

    public Long delete(Long id, String current) {
        Article article = repository.fetchToRate(id);
        article.beforeDelete(current);
        repository.delete(article);
        return article.getTopic().getId();
    }

    private void renewTags(Article article, Set<String> tagNames) {
        Set<Tag> tags = tagNames.stream()
                .map(tagRepository::find)
                .collect(Collectors.toSet());

        Set<ArticleTag> articleTags = article.getArticleTags();
        articleTags.clear();
        for (Tag tag : tags) {
            ArticleTag articleTag = new ArticleTag(article, tag);
            articleTags.add(articleTag);
        }
    }

}
