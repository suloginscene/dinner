package me.scene.dinner.board.article.command.application;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.domain.Article;

@RequiredArgsConstructor
@Getter @EqualsAndHashCode
public class ArticleTaggedEvent {

    private final Article article;

    private final String tagName;

}
