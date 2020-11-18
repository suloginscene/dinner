package me.scene.dinner.board.magazine.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.common.exception.NotDeletableException;
import me.scene.dinner.common.exception.NotOwnerException;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @EqualsAndHashCode(of = "id", callSuper = false)
public class Magazine extends AbstractAggregateRoot<Magazine> {

    @Id @GeneratedValue
    private Long id;

    @JsonIgnore
    private String manager;

    @ElementCollection(fetch = LAZY) @JsonIgnore
    private final List<String> writers = new ArrayList<>();

    @ElementCollection(fetch = LAZY) @JsonIgnore
    private final List<String> authorizedWriters = new ArrayList<>();

    private String title;

    @JsonIgnore
    private String shortExplanation;

    @JsonIgnore
    private String longExplanation;

    @JsonIgnore @Enumerated(EnumType.STRING)
    private Policy policy;

    @OneToMany(mappedBy = "magazine") @JsonIgnore
    private final List<Topic> topics = new ArrayList<>();


    protected Magazine() {
    }

    public static Magazine create(String manager, String title, String shortExplanation, String longExplanation, String magazinePolicy) {
        Magazine magazine = new Magazine();
        magazine.manager = manager;
        magazine.title = title;
        magazine.shortExplanation = shortExplanation;
        magazine.longExplanation = longExplanation;
        magazine.policy = Policy.valueOf(magazinePolicy);
        magazine.registerEvent(new MagazineChangedEvent(magazine));
        return magazine;
    }

    public void update(String current, String title, String shortExplanation, String longExplanation) {
        confirmManager(current);
        this.title = title;
        this.shortExplanation = shortExplanation;
        this.longExplanation = longExplanation;
        registerEvent(new MagazineChangedEvent(this));
    }

    public void confirmManager(String current) {
        if (!current.equals(manager)) throw new NotOwnerException(current);
    }

    public void register(String writer) {
        if (writers.contains(writer)) return;

        writers.add(writer);
    }

    public void registerAsAuthorizedWriter(String writer) {
        if (policy != Policy.MANAGED) throw new IllegalStateException("Not Managed Magazine");
        if (authorizedWriters.contains(writer)) return;

        authorizedWriters.add(writer);
    }

    public void checkAuthorization(String username) {

        if (policy == Policy.OPEN) {
            return;
        }

        if (policy == Policy.EXCLUSIVE) {
            if (manager.equals(username)) return;
        }

        if (policy == Policy.MANAGED) {
            if (manager.equals(username)) return;
            if (authorizedWriters.contains(username)) return;
        }

        throw new PolicyAuthException(username);

    }

    public void confirmDeletable() {
        if (topics.isEmpty()) return;
        throw new NotDeletableException(title);
    }

    public void add(Topic topic) {
        topics.add(topic);
    }

    public void remove(Topic topic) {
        topics.remove(topic);
    }

    public void registerDeletedEvent() {
        registerEvent(new MagazineChangedEvent(this, true));
    }

}
