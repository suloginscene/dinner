package me.scene.dinner.board.article.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
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

    private Long writerId;

    private String title;

    @Lob
    private String content;

    private LocalDateTime localDateTime;

    protected Article() {
    }

    public static Article create(Topic topic, Long writerId, String title, String content) {
        Article article = new Article();
        topic.add(article);
        article.topic = topic;
        article.writerId = writerId;
        article.title = title;
        article.content = content;
        article.localDateTime = LocalDateTime.now();
        return article;
    }

}
