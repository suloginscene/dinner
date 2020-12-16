package me.scene.dinner.board.topic.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.board.common.Board;
import me.scene.dinner.board.common.NotDeletableException;
import me.scene.dinner.board.common.Owner;
import me.scene.dinner.board.magazine.domain.Magazine;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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

    private int articleCount;

    public Topic(Magazine magazine, String owner, String title, String shortExplanation, String longExplanation) {
        magazine.checkAuthorization(owner);
        magazine.addTopic();
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
        if (articleCount != 0) throw new NotDeletableException(title);
        magazine.removeTopic();
    }

    public void addArticle() {
        articleCount++;
    }

    public void removeArticle() {
        articleCount--;
    }

    public void rate(int point) {
        rating += point;
        magazine.rate(point);
    }

}
