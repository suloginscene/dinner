package me.scene.dinner.board.article.application.query;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.application.query.dto.ArticleExtendedLink;
import me.scene.dinner.board.article.application.query.dto.ArticleView;
import me.scene.dinner.board.article.domain.article.model.Article;
import me.scene.dinner.board.article.domain.article.repository.ArticleRepository;
import me.scene.dinner.board.common.domain.model.Owner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleQueryService {

    private final ArticleRepository repository;


    public ArticleView find(Long id) {
        Article article = repository.find(id);
        return new ArticleView(article);
    }


    public List<ArticleExtendedLink> findPublicByWriter(String username) {
        Owner key = new Owner(username);
        List<Article> articles = repository.findByOwnerAndPublicizedOrderByPointDesc(key, true);
        return articles.stream()
                .map(ArticleExtendedLink::new)
                .collect(toList());
    }

    public List<ArticleExtendedLink> findPrivateByWriter(String username) {
        Owner key = new Owner(username);
        List<Article> articles = repository.findByOwnerAndPublicizedOrderByCreatedAtAsc(key, false);
        return articles.stream()
                .map(ArticleExtendedLink::new)
                .collect(toList());
    }

    public List<ArticleExtendedLink> findArticles(Long topicId) {
        List<Article> topics = repository.findByTopicId(topicId);
        return topics.stream()
                .filter(Article::isPublicized)
                .map(ArticleExtendedLink::new)
                .collect(toList());
    }

}
