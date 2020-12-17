package me.scene.dinner.board.magazine.domain.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.board.common.Board;
import me.scene.dinner.board.common.NotDeletableException;
import me.scene.dinner.board.common.Owner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.InheritanceType.JOINED;
import static lombok.AccessLevel.PROTECTED;


@Entity
@Inheritance(strategy = JOINED)
@NoArgsConstructor(access = PROTECTED)
public abstract class Magazine extends Board {

    @Getter @Column(nullable = false, length = 20)
    private String title;

    @Getter @Column(nullable = false, length = 30)
    private String shortExplanation;

    @Getter
    private String longExplanation;

    private int topicCount;


    public abstract String type();


    public void checkAuthorization(String username) throws AccessException {
    }

    public List<String> memberNames() {
        return new ArrayList<>();
    }

    public List<String> writerNames() {
        return new ArrayList<>();
    }

    public void logWriting(String writerName) {
    }

    public void logErasing(String writerName) {
    }


    protected Magazine(Owner owner, String title, String shortExplanation, String longExplanation) {
        this.owner = owner;
        this.title = title;
        this.shortExplanation = shortExplanation;
        this.longExplanation = longExplanation;
    }


    public void update(String current, String title, String shortExplanation, String longExplanation) {
        owner.identify(current);
        this.title = title;
        this.shortExplanation = shortExplanation;
        this.longExplanation = longExplanation;
    }

    public void beforeDelete(String current) {
        owner.identify(current);
        if (hasChild()) throw new NotDeletableException(title);
    }


    public void addTopic() {
        topicCount++;
    }

    public void removeTopic() {
        topicCount--;
    }

    public boolean hasChild() {
        return topicCount != 0;
    }

}
