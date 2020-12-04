package me.scene.dinner.board.domain.topic;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.scene.dinner.board.domain.article.Article;
import me.scene.dinner.board.domain.common.NotDeletableException;
import me.scene.dinner.board.domain.common.NotOwnerException;
import me.scene.dinner.board.domain.magazine.Magazine;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @EqualsAndHashCode(of = "id")
public class Topic {

    @Id @GeneratedValue
    private Long id;

    private String manager;


    private String title;

    private String shortExplanation;

    private String longExplanation;


    private int rating;


    @ManyToOne(fetch = LAZY)
    private Magazine magazine;

    @OneToMany(mappedBy = "topic")
    private final List<Article> articles = new ArrayList<>();

    public List<Article> getPublicArticles() {
        return articles.stream().filter(Article::isPublic).collect(Collectors.toList());
    }

    public List<Article> getPrivateArticles() {
        return articles.stream().filter(Predicate.not(Article::isPublic)).collect(Collectors.toList());
    }


    protected Topic() {
    }

    public static Topic create(Magazine magazine, String manager, String title, String shortExplanation, String longExplanation) {
        magazine.checkAuthorization(manager);
        Topic topic = new Topic();
        topic.manager = manager;
        topic.title = title;
        topic.shortExplanation = shortExplanation;
        topic.longExplanation = longExplanation;
        topic.magazine = magazine;
        magazine.add(topic);
        return topic;
    }

    public void update(String current, String title, String shortExplanation, String longExplanation) {
        confirmManager(current);
        this.title = title;
        this.shortExplanation = shortExplanation;
        this.longExplanation = longExplanation;
    }

    public void rate(int point) {
        rating += point;
        magazine.rate(point);
    }

    public void beforeDelete(String current) {
        confirmManager(current);
        confirmDeletable();
        magazine.remove(this);
    }


    public void confirmManager(String current) {
        if (current.equals(manager)) return;
        throw new NotOwnerException(current);
    }

    private void confirmDeletable() {
        if (articles.isEmpty()) return;
        throw new NotDeletableException(title);
    }


    public void add(Article article) {
        articles.add(article);
    }

    public void remove(Article article) {
        articles.remove(article);
    }

}
