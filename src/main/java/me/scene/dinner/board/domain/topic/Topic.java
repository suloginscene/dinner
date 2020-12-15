package me.scene.dinner.board.domain.topic;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.board.domain.article.Article;
import me.scene.dinner.board.domain.common.Board;
import me.scene.dinner.board.domain.common.NotDeletableException;
import me.scene.dinner.board.domain.common.Owner;
import me.scene.dinner.board.domain.magazine.Magazine;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Topic extends Board {

    private String title;

    private String shortExplanation;

    private String longExplanation;


    private int rating;


    @ManyToOne(fetch = LAZY)
    private Magazine magazine;

    @OneToMany(mappedBy = "topic")
    private final List<Article> articles = new ArrayList<>();

    public List<Article> getPublicArticles() {
        return articles.stream().filter(Article::isPublicized).collect(Collectors.toList());
    }


    public Topic(Magazine magazine, String owner, String title, String shortExplanation, String longExplanation) {
        magazine.checkAuthorization(owner);
        magazine.add(this);
        this.owner = new Owner(owner);
        this.title = title;
        this.shortExplanation = shortExplanation;
        this.longExplanation = longExplanation;
        this.magazine = magazine;
    }

    public void update(String current, String title, String shortExplanation, String longExplanation) {
        owner.identify(current);
        this.title = title;
        this.shortExplanation = shortExplanation;
        this.longExplanation = longExplanation;
    }

    public void beforeDelete(String current) {
        owner.identify(current);
        if (!articles.isEmpty()) throw new NotDeletableException(title);
        magazine.remove(this);
    }

    public void rate(int point) {
        rating += point;
        magazine.rate(point);
    }


    public void add(Article article) {
        articles.add(article);
    }

    public void remove(Article article) {
        articles.remove(article);
    }

    public MagazineSummary magazineSummary() {
        return new MagazineSummary(magazine.getId(), magazine.getOwner().getOwnerName(), magazine.getTitle(),
                magazine.getPolicy().name(), magazine.getMembers());
    }

    public List<ArticleSummary> privateArticleSummaries() {
        return articles.stream().filter(Predicate.not(Article::isPublicized))
                .map(a -> new ArticleSummary(a.getId(), a.getOwner().getOwnerName(), a.getTitle(), a.getContent(), a.getCreatedAt()))
                .sorted(Comparator.comparing(ArticleSummary::getCreatedAt))
                .collect(Collectors.toList());
    }

    public List<ArticleSummary> publicArticleSummaries() {
        return articles.stream().filter(Article::isPublicized)
                .map(a -> new ArticleSummary(a.getId(), a.getOwner().getOwnerName(), a.getTitle(), a.getContent(), a.getCreatedAt()))
                .sorted(Comparator.comparing(ArticleSummary::getCreatedAt))
                .collect(Collectors.toList());
    }

}
