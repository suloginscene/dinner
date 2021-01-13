package me.scene.paper.service.board.topic.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.paper.service.board.common.domain.model.Board;
import me.scene.paper.service.board.common.domain.model.Children;
import me.scene.paper.service.board.magazine.domain.magazine.model.Magazine;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;


@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Topic extends Board {

    @Column(length = 30, nullable = false)
    private String shortExplanation;

    private String longExplanation;

    @Embedded
    private final Children articles = new Children();


    @ManyToOne(fetch = LAZY)
    private Magazine magazine;


    public Topic(Magazine magazine, String owner, String title, String shortExplanation, String longExplanation) {
        super(title, owner);
        magazine.authorize(owner);
        this.shortExplanation = shortExplanation;
        this.longExplanation = longExplanation;
        this.magazine = magazine;
        magazine.addTopic();
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
        magazine.removeTopic();
    }


    public void addArticle() {
        articles.add();
    }

    public void removeArticle() {
        articles.remove();
    }

    public boolean hasArticle() {
        return articles.exists();
    }


    public void propagateAuthorize(String username) {
        magazine.authorize(username);
    }

    public void propagateLogWriter(String writer, boolean publicized) {
        magazine.logWriter(writer, publicized);
    }

    @Override
    protected void propagateRate(int point) {
        magazine.rate(point);
    }

}
