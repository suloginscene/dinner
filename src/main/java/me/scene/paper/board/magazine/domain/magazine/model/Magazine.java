package me.scene.paper.board.magazine.domain.magazine.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.paper.board.common.domain.model.Board;
import me.scene.paper.board.common.domain.model.Owner;
import me.scene.paper.board.common.domain.model.ToManyInfo;
import me.scene.paper.board.magazine.domain.exclusive.model.ExclusiveMagazine;
import me.scene.paper.board.magazine.domain.managed.model.ManagedMagazine;
import me.scene.paper.board.magazine.domain.open.model.OpenMagazine;

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

    @Column(length = 30, nullable = false)
    private String shortExplanation;

    private String longExplanation;

    @Embedded
    private final ToManyInfo topics = new ToManyInfo();


    public abstract Type type();

    public abstract Authorization authorization();


    protected Magazine(String owner, String title, String shortExplanation, String longExplanation) {
        this.owner = new Owner(owner);
        this.title = title;
        this.shortExplanation = shortExplanation;
        this.longExplanation = longExplanation;
    }

    public static Magazine create(Type type, String owner, String title, String shortExplanation, String longExplanation) {
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


    public void update(String current, String title, String shortExplanation, String longExplanation) {
        owner.identify(current);

        this.title = title;
        this.shortExplanation = shortExplanation;
        this.longExplanation = longExplanation;
    }

    public void beforeDelete(String current) {
        owner.identify(current);
        topics.emptyCheck();
    }


    @Override
    protected void propagateRate(int point) {
    }

}
