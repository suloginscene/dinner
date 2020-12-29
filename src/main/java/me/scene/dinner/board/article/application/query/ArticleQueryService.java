package me.scene.dinner.board.article.application.query;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.application.query.dto.ArticleExtendedLink;
import me.scene.dinner.board.article.application.query.dto.ArticleView;
import me.scene.dinner.board.article.domain.article.model.Article;
import me.scene.dinner.board.article.domain.article.model.Like;
import me.scene.dinner.board.article.domain.article.repository.ArticleRepository;
import me.scene.dinner.board.common.domain.model.Owner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;


@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleQueryService {

    private final ArticleRepository repository;


    public ArticleView view(Long id) {
        Article article = repository.fetchToView(id);
        return new ArticleView(article);
    }


    public List<ArticleExtendedLink> findPublicByWriter(String username) {
        Owner owner = new Owner(username);
        List<Article> articles = repository.findByUsername(owner);
        return articles.stream()
                .map(ArticleExtendedLink::new)
                .collect(toList());
    }

    public List<ArticleExtendedLink> findPrivateByWriter(String username) {
        Owner owner = new Owner(username);
        List<Article> articles = repository.findPrivateByUsername(owner);
        return articles.stream()
                .map(ArticleExtendedLink::new)
                .collect(toList());
    }

    public List<ArticleExtendedLink> linksOfTopic(Long topicId) {
        List<Article> articles = repository.findPublicByTopicId(topicId);
        return articles.stream()
                .map(ArticleExtendedLink::new)
                .collect(toList());
    }

    public boolean doesLike(String username, Long articleId) {
        Set<Like> likes = repository.find(articleId).getLikes();
        return likes.contains(new Like(username));
    }

}
