package me.scene.dinner.domain.board.article;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.scene.dinner.domain.account.Account;
import me.scene.dinner.domain.board.topic.Topic;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;

@Entity
@Getter @EqualsAndHashCode(of = "id")
public class Article {

    protected Article() {
    }

    public Article(Account current, Topic topic, ArticleForm articleForm) {
        this.writer = current.getId();
        this.topic = topic;
        this.url = articleForm.getUrl();
        this.title = articleForm.getTitle();
        this.content = articleForm.getContent();
        this.date = LocalDateTime.now();
    }

    @Id @GeneratedValue
    private Long id;

    private Long writer;

    @ManyToOne(fetch = LAZY)
    private Topic topic;

    private String url;

    private String title;

    private String content;

    private LocalDateTime date;

}
