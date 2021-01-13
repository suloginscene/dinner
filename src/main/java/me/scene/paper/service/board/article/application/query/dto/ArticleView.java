package me.scene.paper.service.board.article.application.query.dto;

import lombok.Getter;
import me.scene.paper.service.board.article.domain.article.model.Article;
import me.scene.paper.service.board.common.dto.View;
import me.scene.paper.service.board.topic.application.query.dto.TopicLink;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;


@Getter
public class ArticleView extends View {

    private final String content;

    private final boolean publicized;

    private final int read;
    private final int like;

    private final TopicLink topic;
    private final List<ReplyView> replies;
    private final Set<String> tags;


    public ArticleView(Article article) {
        super(article);

        this.content = article.getContent();
        this.publicized = article.isPublicized();

        this.read = article.getRead();
        this.like = article.getLikes().size();

        this.topic = new TopicLink(article.getTopic());
        this.replies = article.getReplies().stream()
                .map(ReplyView::new)
                .collect(toList());
        this.tags = article.getArticleTags().stream()
                .map(articleTag -> articleTag.getTag().getName())
                .collect(toSet());
    }

}
