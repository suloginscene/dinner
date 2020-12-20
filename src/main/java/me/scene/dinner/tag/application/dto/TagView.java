package me.scene.dinner.tag.application.dto;

import lombok.Data;
import me.scene.dinner.board.article.application.query.dto.ArticleExtendedLink;
import me.scene.dinner.tag.domain.Tag;
import me.scene.dinner.tag.domain.TaggedArticle;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Data
public class TagView {

    private final String name;
    private final List<ArticleExtendedLink> articles;

    public TagView(Tag tag) {
        this.name = tag.getName();
        this.articles = tag.getPublicizedTaggedArticles().stream()
                .map(TaggedArticle::getArticle)
                .map(ArticleExtendedLink::new)
                .collect(toList());
    }

}
