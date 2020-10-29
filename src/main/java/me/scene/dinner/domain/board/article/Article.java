package me.scene.dinner.domain.board.article;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.scene.dinner.domain.account.Account;
import me.scene.dinner.domain.board.topic.Topic;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @EqualsAndHashCode(of = "id")
public class Article {

    protected Article() {
    }

    public Article(Account current, Topic topic, ArticleForm articleForm) {
        this.writer = current.getId();
        this.topic = topic;
        this.title = articleForm.getTitle();
        this.content = articleForm.getContent();
    }

    @Id @GeneratedValue
    private Long id;

    private Long writer;

    @ManyToOne(fetch = LAZY)
    private Topic topic;

    private String title;

    private String content;

}
