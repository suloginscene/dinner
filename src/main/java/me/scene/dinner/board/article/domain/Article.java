package me.scene.dinner.board.article.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.scene.dinner.board.article.application.PublicationException;
import me.scene.dinner.board.topic.domain.Topic;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    protected Article() {
    }

    public static Article create(Topic topic, String writer, String title, String content) {
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

    public void checkPublicity(String client) {
        if (!isPublished() && !client.equals(writer)) throw new PublicationException(id);
    }

    public void publish(String client) {
        if (!client.equals(writer)) throw new PublicationException(id);
        published = true;
    }

}
