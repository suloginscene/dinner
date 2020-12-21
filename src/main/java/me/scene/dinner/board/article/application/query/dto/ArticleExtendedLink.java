package me.scene.dinner.board.article.application.query.dto;

import lombok.Getter;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.common.dto.Link;

import java.time.LocalDateTime;


@Getter
public class ArticleExtendedLink extends Link {

    private final String owner;

    private final Integer point;
    private final int read;
    private final int like;

    private final LocalDateTime createdAt;
    private final boolean publicized;


    public ArticleExtendedLink(Article article) {
        super(article);

        this.owner = article.getOwner().getName();

        this.point = article.getPoint().getPoint();
        this.read = article.getRead();
        this.like = article.getLike();

        this.createdAt = article.getCreatedAt();
        this.publicized = article.isPublicized();
    }

}