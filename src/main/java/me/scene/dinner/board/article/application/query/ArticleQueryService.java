package me.scene.dinner.board.article.application.query;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.application.query.dto.ArticleExtendedLink;
import me.scene.dinner.board.article.application.query.dto.ArticleView;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.article.domain.ArticleRepository;
import me.scene.dinner.board.common.domain.Owner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleQueryService {

    private final ArticleRepository repository;


    @Transactional
    public ArticleView read(Long id, String username) {
        Article article = repository.find(id);

        if (article.isPublicized()) article.read();
        else article.getOwner().identify(username);

        return new ArticleView(article);
    }

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

}
