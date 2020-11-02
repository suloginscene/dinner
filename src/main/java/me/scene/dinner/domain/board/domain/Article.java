package me.scene.dinner.domain.board.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.scene.dinner.domain.account.domain.Account;
import me.scene.dinner.domain.board.ui.ArticleForm;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @EqualsAndHashCode(of = "id")
public class Article {

    protected Article() {
    }

    public Article(Account current, Topic topic, ArticleForm articleForm) {
        this.writer = current.getId();
        this.topic = topic;
        this.url = articleForm.getParentUrl() + articleForm.getUrl();
        this.title = articleForm.getTitle();
        this.content = articleForm.getContent();
        this.date = LocalDateTime.now();
    }

    @Id @GeneratedValue
    private Long id;

    private Long writer;

    @ManyToOne(fetch = LAZY)
    private Topic topic;

    @Column(unique = true)
    private String url;

    private String title;

    @Lob
    private String content;

    private LocalDateTime date;

}
