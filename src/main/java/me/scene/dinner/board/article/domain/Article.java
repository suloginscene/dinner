package me.scene.dinner.board.article.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.scene.dinner.board.reply.domain.Reply;
import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.common.exception.NotOwnerException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @EqualsAndHashCode(of = "id")
public class Article {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    private Topic topic;

    private String writer;

    private String title;

    @Lob
    private String content;

    private boolean published;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "article")
    private final List<Reply> replies = new ArrayList<>();

    protected Article() {
    }

    public static Article create(Topic topic, String writer, String title, String content) {
        topic.getMagazine().checkAuthorization(writer);

        Article article = new Article();
        topic.add(article);
        article.topic = topic;
        article.writer = writer;
        article.title = title;
        article.content = content;
        article.published = false;
        article.createdAt = LocalDateTime.now();
        return article;
    }

    public void update(String current, String title, String content) {
        confirmWriter(current);
        this.title = title;
        this.content = content;
    }

    public void confirmWriter(String current) {
        if (!current.equals(writer)) throw new NotOwnerException(current);
    }

    public void publish(String current) {
        confirmWriter(current);
        published = true;
        topic.register(writer);
    }

    public void add(Reply reply) {
        replies.add(reply);
    }

}
