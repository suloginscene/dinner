package me.scene.paper.board.article.application.query.dto;

import lombok.Data;
import me.scene.paper.board.article.domain.tag.model.Tag;
import me.scene.paper.board.article.domain.article.model.ArticleTag;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Data
public class TagView {

    private final String name;
    private final List<ArticleInTag> articles;

    public TagView(Tag tag) {
        this.name = tag.getName();
        this.articles = tag.publicTaggedArticles().stream()
                .map(ArticleTag::getArticle)
                .map(ArticleInTag::new)
                .collect(toList());
    }

}
