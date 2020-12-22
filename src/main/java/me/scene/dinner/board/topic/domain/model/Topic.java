package me.scene.dinner.board.topic.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.board.common.domain.model.Board;
import me.scene.dinner.board.common.domain.model.Owner;
import me.scene.dinner.board.common.domain.model.ToManyInfo;
import me.scene.dinner.board.magazine.domain.magazine.model.Magazine;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;


@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Topic extends Board {

    private String shortExplanation;

    private String longExplanation;

    @Embedded
    private final ToManyInfo articles = new ToManyInfo();


    @ManyToOne(fetch = LAZY)
    private Magazine magazine;


    public Topic(Magazine magazine, String owner, String title, String shortExplanation, String longExplanation) {
        magazine.authorization().check(owner);
        magazine.getTopics().add();
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
        articles.emptyCheck();
    }

    @Override
    protected void propagateRate(int point) {
        magazine.rate(point);
    }

}
