package me.scene.dinner.board.application.article;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.domain.article.Article;

@RequiredArgsConstructor
@Getter @EqualsAndHashCode
public class ArticleTaggedEvent {

    private final Article article;

    private final String tagName;

}
