package me.scene.dinner.board.article.application;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import me.scene.dinner.board.article.domain.Reply;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ArticleSimpleDto {

    private final Long id;

    @JsonIgnore
    private final String writer;

    private final String title;

    @JsonIgnore
    private final String content;

    @JsonIgnore
    private final String status;

    private final LocalDateTime createdAt;

    private final int read;

    private final int likes;

    @JsonIgnore
    private final TopicSummary topic;

    @JsonIgnore
    private final List<Reply> replies;

    public ArticleSimpleDto(Long id, String writer, String title, String content, boolean publicized, LocalDateTime createdAt, int read, int likes, TopicSummary topic, List<Reply> replies) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.status = (publicized) ? "PUBLIC" : "PRIVATE";
        this.createdAt = createdAt;
        this.read = read;
        this.likes = likes;
        this.topic = topic;
        this.replies = replies;
    }

}
