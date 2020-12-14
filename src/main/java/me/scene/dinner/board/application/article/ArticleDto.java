package me.scene.dinner.board.application.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import me.scene.dinner.board.domain.article.ReplySummary;
import me.scene.dinner.board.domain.article.TopicSummary;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
public class ArticleDto {

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
    private final Set<String> tags;

    @JsonIgnore
    private final TopicSummary topic;

    @JsonIgnore
    private final List<ReplySummary> replies;

    public ArticleDto(Long id, String writer, String title, String content, boolean publicized, LocalDateTime createdAt, int read, int likes, Set<String> tags, TopicSummary topic, List<ReplySummary> replies) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.status = (publicized) ? "PUBLIC" : "PRIVATE";
        this.createdAt = createdAt;
        this.read = read;
        this.likes = likes;
        this.tags = tags;
        this.topic = topic;
        this.replies = replies;
    }

}
