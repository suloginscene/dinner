package me.scene.paper.board.magazine.domain.magazine.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.paper.board.common.domain.model.Board;
import me.scene.paper.board.common.domain.model.Children;
import me.scene.paper.board.magazine.domain.exclusive.model.ExclusiveMagazine;
import me.scene.paper.board.magazine.domain.managed.model.ManagedMagazine;
import me.scene.paper.board.magazine.domain.open.model.OpenMagazine;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.InheritanceType.JOINED;
import static lombok.AccessLevel.PROTECTED;
import static me.scene.paper.board.magazine.domain.magazine.model.Type.MANAGED;
import static me.scene.paper.board.magazine.domain.magazine.model.Type.OPEN;


@Entity
@Inheritance(strategy = JOINED)
@Getter
@NoArgsConstructor(access = PROTECTED)
public abstract class Magazine extends Board {

    @Column(length = 30, nullable = false)
    private String shortExplanation;

    private String longExplanation;

    @Embedded
    private final Children topics = new Children();


    protected Magazine(String owner, String title, String shortExplanation, String longExplanation) {
        super(title, owner);
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


    public void addTopic() {
        topics.add();
    }

    public void removeTopic() {
        topics.remove();
    }

    public boolean hasTopic() {
        return topics.exists();
    }


    public List<String> writerNames() {
        if (!is(OPEN)) return new ArrayList<>();

        OpenMagazine open = (OpenMagazine) this;
        return open.writerNames();
    }

    public void logWriter(String writer, boolean publicized) {
        if (owner.is(writer)) return;
        if (!is(OPEN)) return;

        OpenMagazine open = (OpenMagazine) this;
        if (publicized) open.logWriting(writer);
        else open.logErasing(writer);
    }


    public List<String> memberNames() {
        if (!is(MANAGED)) return new ArrayList<>();

        ManagedMagazine managed = (ManagedMagazine) this;
        return managed.memberNames();
    }

    public boolean manageMember(String username, String member, boolean add) {
        owner.identify(username);
        typeCheck(MANAGED);

        ManagedMagazine managed = (ManagedMagazine) this;
        return (add) ? managed.addMember(member) : managed.removeMember(member);
    }

    public boolean actAsMember(String username, boolean apply) {
        typeCheck(MANAGED);

        ManagedMagazine managed = (ManagedMagazine) this;
        return (apply) ? managed.apply(username) : managed.quit(username);
    }


    protected abstract Type type();

    public boolean is(Type expected) {
        return type() == expected;
    }

    public String typeName() {
        return type().name();
    }

    public void typeCheck(Type expected) {
        type().check(expected);
    }


    protected abstract Authorization authorization();

    public void authorize(String username) {
        authorization().check(username);
    }


    @Override
    protected void propagateRate(int point) {
    }

}
