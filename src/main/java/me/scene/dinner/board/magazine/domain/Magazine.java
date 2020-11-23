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

    @JsonIgnore
    private String managerEmail;

    private String title;

    @JsonIgnore
    private String shortExplanation;

    @JsonIgnore
    private String longExplanation;

    @JsonIgnore @Enumerated(EnumType.STRING)
    private Policy policy;


    @OneToMany(mappedBy = "magazine") @JsonIgnore
    private final List<Topic> topics = new ArrayList<>();


    @ElementCollection(fetch = LAZY) @JsonIgnore
    private final List<String> writers = new ArrayList<>();

    @ElementCollection(fetch = LAZY) @JsonIgnore
    private final List<String> members = new ArrayList<>();

    @JsonIgnore
    public boolean isOpen() {
        return policy == Policy.OPEN;
    }

    @JsonIgnore
    public boolean isManaged() {
        return policy == Policy.MANAGED;
    }

    public boolean doesAccept(String current) {
        if (policy == Policy.OPEN) return true;
        if (policy == Policy.EXCLUSIVE) return current.equals(manager);
        if (policy == Policy.MANAGED) return (current.equals(manager) || members.contains(current));
        throw new IllegalStateException("Magazine should have policy in enum");
    }

    protected Magazine() {
    }

    public static Magazine create(String manager, String managerEmail, String title, String shortExplanation, String longExplanation, String magazinePolicy) {
        Magazine magazine = new Magazine();
        magazine.manager = manager;
        magazine.managerEmail = managerEmail;
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

    public void beforeDelete(String current) {
        confirmManager(current);
        confirmDeletable();
        registerEvent(new MagazineChangedEvent(this, true));
    }


    public void confirmManager(String current) {
        if (current.equals(manager)) return;
        throw new NotOwnerException(current);
    }

    private void confirmDeletable() {
        if (topics.isEmpty()) return;
        throw new NotDeletableException(title);
    }


    public void add(Topic topic) {
        topics.add(topic);
    }

    public void remove(Topic topic) {
        topics.remove(topic);
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
            if (members.contains(username)) return;
        }

        throw new PolicyAuthException(username);

    }

    public void addWriter(String writer) {
        if (writers.contains(writer)) return;
        writers.add(writer);
    }

    public void removeWriter(String writer) {
        long count = topics.stream().map(Topic::getArticles).flatMap(List::stream)
                .filter(a -> a.getWriter().equals(writer)).count();
        if (count != 1) return;
        writers.remove(writer);
    }

    public void confirmPolicyManaged() {
        if (isManaged()) return;
        throw new IllegalStateException("Not Managed Magazine");
    }

    public void applyMember(String current) {
        confirmPolicyManaged();
        if (members.contains(current)) return;

        registerEvent(new MemberAppliedEvent(this, id, managerEmail, current));
    }

    public void quitMember(String current) {
        confirmPolicyManaged();
        if (!members.contains(current)) return;

        members.remove(current);
        registerEvent(new MemberQuitEvent(this, id, managerEmail, current));
    }

    public void addMember(String current, String target) {
        confirmManager(current);
        confirmPolicyManaged();
        if (members.contains(target)) return;

        members.add(target);
        // TODO event(send async to target)
    }

    public void removeMember(String current, String target) {
        confirmManager(current);
        confirmPolicyManaged();
        if (!members.contains(target)) return;

        members.remove(target);
        // TODO event(send async to target)
    }

}
