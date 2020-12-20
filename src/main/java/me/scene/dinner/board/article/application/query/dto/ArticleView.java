package me.scene.dinner.board.article.application.query.dto;

import lombok.Getter;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.article.domain.Reply;
import me.scene.dinner.board.common.dto.View;
import me.scene.dinner.board.topic.application.query.dto.TopicLink;

import java.util.List;


@Getter
public class ArticleView extends View {

    private final String content;

    private final boolean publicized;

    private final int read;
    private final int like;

    private final TopicLink topic;
    private final List<Reply> replies;


    public ArticleView(Article article) {
        super(article);

        this.content = article.getContent();
        this.publicized = article.isPublicized();

        this.read = article.getRead();
        this.like = article.getLike();

        this.topic = new TopicLink(article.getTopic());
        this.replies = article.getReplies();
    }

}

