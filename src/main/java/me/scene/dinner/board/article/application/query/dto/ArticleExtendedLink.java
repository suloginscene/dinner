package me.scene.dinner.board.article.application.query.dto;

import lombok.Getter;
import me.scene.dinner.board.article.domain.article.model.Article;
import me.scene.dinner.board.common.dto.Link;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;


@Getter
public class ArticleExtendedLink extends Link {

    private final String owner;

    private final Integer point;
    private final int read;
    private final int like;

    private final LocalDateTime createdAt;
    private final boolean publicized;

    private final List<String> tags;


    public ArticleExtendedLink(Article article) {
        super(article);

        this.owner = article.getOwner().name();

        this.point = article.getPoint().get();
        this.read = article.getRead();
        this.like = article.getLikes().size();

        this.createdAt = article.getCreatedAt();
        this.publicized = article.isPublicized();
        tags = article.getArticleTags()
                .stream().map(articleTag -> articleTag.getTag().getName())
                .collect(toList());
    }

}
