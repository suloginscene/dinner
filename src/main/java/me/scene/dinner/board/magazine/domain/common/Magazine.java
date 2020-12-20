package me.scene.dinner.board.magazine.domain.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.board.common.domain.Board;
import me.scene.dinner.board.common.domain.Owner;
import me.scene.dinner.board.magazine.domain.exclusive.ExclusiveMagazine;
import me.scene.dinner.board.magazine.domain.managed.ManagedMagazine;
import me.scene.dinner.board.magazine.domain.open.OpenMagazine;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;

import static javax.persistence.InheritanceType.JOINED;
import static lombok.AccessLevel.PROTECTED;


@Entity
@Inheritance(strategy = JOINED)
@Getter
@NoArgsConstructor(access = PROTECTED)
public abstract class Magazine extends Board {

    @Column(nullable = false, length = 30)
    private String shortExplanation;

    private String longExplanation;

    @Embedded
    private final Topics topics = new Topics();


    public abstract Type type();

    public abstract Authorization authorization();


    protected Magazine(Owner owner, String title, String shortExplanation, String longExplanation) {
        this.owner = owner;
        this.title = title;
        this.shortExplanation = shortExplanation;
        this.longExplanation = longExplanation;
    }

    public static Magazine create(Type type, Owner owner, String title, String shortExplanation, String longExplanation) {
        switch (type) {
            case OPEN:
                return new OpenMagazine(owner, title, shortExplanation, longExplanation);
            case MANAGED:
                return new ManagedMagazine(owner, title, shortExplanation, longExplanation);
            case EXCLUSIVE:
                return new ExclusiveMagazine(owner, title, shortExplanation, longExplanation);
        }
        throw new IllegalStateException("Type not defined.");
    }


    public ChangedEvent update(String current, String title, String shortExplanation, String longExplanation) {
        owner.identify(current);

        this.title = title;
        this.shortExplanation = shortExplanation;
        this.longExplanation = longExplanation;

        return new ChangedEvent(id);
    }

    public ChangedEvent beforeDelete(String current) {
        owner.identify(current);
        topics.emptyCheck();
        return new ChangedEvent(id);
    }

    public ChangedEvent changedEvent() {
        return new ChangedEvent(id);
    }

    @Override
    protected void propagateRate(int point) {
    }

}
