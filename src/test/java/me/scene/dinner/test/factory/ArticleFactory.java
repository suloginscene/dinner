package me.scene.dinner.test.factory;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.application.ArticleService;
import me.scene.dinner.board.article.domain.Article;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleFactory {

    private final ArticleService articleService;

    public Article create(Long topicId, String writer, String title, String content) {
        Long id = articleService.save(topicId, writer, title, content);
        Article article = articleService.find(id);
        articleService.publish(id, writer);
        return article;
    }

}
