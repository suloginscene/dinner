package me.scene.dinner.board.article.application.command;

import lombok.Data;
import me.scene.dinner.board.article.domain.Article;

import java.util.Set;


@Data
public class ArticleTaggedEvent {

    private final Article article;
    private final Set<String> tagNames;

}
