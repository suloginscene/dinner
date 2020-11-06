package me.scene.dinner.board.topic.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.magazine.domain.Magazine;

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

    private Long managerId;

    private String title;

    private String shortExplanation;

    private String longExplanation;

    @OneToMany(mappedBy = "topic")
    private final List<Article> articles = new ArrayList<>();


    protected Topic() {
    }

    public static Topic create(Magazine magazine, Long managerId, String title, String shortExplanation, String longExplanation) {
        Topic topic = new Topic();
        magazine.add(topic);
        topic.magazine = magazine;
        topic.managerId = managerId;
        topic.title = title;
        topic.shortExplanation = shortExplanation;
        topic.longExplanation = longExplanation;
        return topic;
    }

    public void add(Article article) {
        articles.add(article);
    }

}
