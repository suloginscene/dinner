package me.scene.dinner.utils.factory;

import me.scene.dinner.board.article.application.ArticleService;
import me.scene.dinner.board.article.domain.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArticleFactory {

    private final ArticleService articleService;

    @Autowired
    public ArticleFactory(ArticleService articleService) {
        this.articleService = articleService;
    }

    public Article create(Long topicId, String writer, String title, String content) {
        Long id = articleService.save(topicId, writer, title, content);
        Article article = articleService.find(id);
        articleService.publish(id, writer);
        return article;
    }

}
