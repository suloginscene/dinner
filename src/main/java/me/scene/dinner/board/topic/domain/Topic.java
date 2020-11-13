package me.scene.dinner.board.topic.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.common.exception.NotOwnerException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @EqualsAndHashCode(of = "id")
public class Topic {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    private Magazine magazine;

    private String manager;

    private String title;

    private String shortExplanation;

    private String longExplanation;

    @OneToMany(mappedBy = "topic")
    private final List<Article> articles = new ArrayList<>();


    protected Topic() {
    }

    public static Topic create(Magazine magazine, String manager, String title, String shortExplanation, String longExplanation) {
        magazine.checkAuthorization(manager);

        Topic topic = new Topic();
        magazine.add(topic);
        topic.magazine = magazine;
        topic.manager = manager;
        topic.title = title;
        topic.shortExplanation = shortExplanation;
        topic.longExplanation = longExplanation;
        return topic;
    }

    public void confirmManager(String current) {
        if (!current.equals(manager)) throw new NotOwnerException(current);
    }

    public void register(String writer) {
        magazine.register(writer);
    }

    public void add(Article article) {
        articles.add(article);
    }

    public void update(String current, String title, String shortExplanation, String longExplanation) {
        confirmManager(current);
        this.title = title;
        this.shortExplanation = shortExplanation;
        this.longExplanation = longExplanation;
    }

}
