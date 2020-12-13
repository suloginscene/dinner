package me.scene.dinner.test.factory;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.application.article.ArticleService;
import me.scene.dinner.board.domain.article.Article;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ArticleFactory {

    private final ArticleService articleService;

    public Article create(Long topicId, String writer, String title, String content, boolean publicized, String... tags) {
        Long id = articleService.save(topicId, writer, title, content, publicized, Set.of(tags));
        return articleService.find(id);
    }

}
