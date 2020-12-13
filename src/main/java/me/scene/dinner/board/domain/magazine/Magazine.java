package me.scene.dinner.board.domain.magazine;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.scene.dinner.board.domain.common.NotDeletableException;
import me.scene.dinner.board.domain.common.NotOwnerException;
import me.scene.dinner.board.domain.magazine.event.MagazineChangedEvent;
import me.scene.dinner.board.domain.magazine.event.MagazineCreatedEvent;
import me.scene.dinner.board.domain.magazine.event.MagazineDeletedEvent;
import me.scene.dinner.board.domain.magazine.event.MemberAddedEvent;
import me.scene.dinner.board.domain.magazine.event.MemberAppliedEvent;
import me.scene.dinner.board.domain.magazine.event.MemberQuitEvent;
import me.scene.dinner.board.domain.magazine.event.MemberRemovedEvent;
import me.scene.dinner.board.domain.topic.Topic;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static me.scene.dinner.board.domain.magazine.Policy.EXCLUSIVE;
import static me.scene.dinner.board.domain.magazine.Policy.MANAGED;
import static me.scene.dinner.board.domain.magazine.Policy.OPEN;

@Entity
@Getter @EqualsAndHashCode(of = "id", callSuper = false)
public class Magazine extends AbstractAggregateRoot<Magazine> {

    @Id @GeneratedValue
    private Long id;

    private String manager;


    private String title;

    private String shortExplanation;

    private String longExplanation;

    @Enumerated(STRING)
    private Policy policy;


    private int rating;


    @OneToMany(mappedBy = "magazine")
    private final List<Topic> topics = new ArrayList<>();


    @ElementCollection(fetch = LAZY)
    private final List<String> writers = new ArrayList<>();

    @ElementCollection(fetch = LAZY)
    private final List<String> members = new ArrayList<>();


    protected Magazine() {
    }

    public static Magazine create(String manager, String title, String shortExplanation, String longExplanation, String magazinePolicy) {
        Magazine magazine = new Magazine();
        magazine.manager = manager;
        magazine.title = title;
        magazine.shortExplanation = shortExplanation;
        magazine.longExplanation = longExplanation;
        magazine.policy = Policy.valueOf(magazinePolicy);
        magazine.registerEvent(new MagazineCreatedEvent());
        return magazine;
    }

    public void update(String current, String title, String shortExplanation, String longExplanation) {
        confirmManager(current);
        this.title = title;
        this.shortExplanation = shortExplanation;
        this.longExplanation = longExplanation;
        registerEvent(new MagazineChangedEvent(id));
    }

    public void rate(int point) {
        rating += point;
        if (rating % 100 == 0) registerEvent(new MagazineChangedEvent(id));
    }

    public void beforeDelete(String current) {
        confirmManager(current);
        confirmDeletable();
        registerEvent(new MagazineDeletedEvent(id));
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

        if (policy == OPEN) {
            return;
        }

        if (policy == EXCLUSIVE) {
            if (manager.equals(username)) return;
        }

        if (policy == MANAGED) {
            if (manager.equals(username)) return;
            if (members.contains(username)) return;
        }

        throw new PolicyAuthorizationException(username);

    }

    public void addWriter(String writer) {
        if (writers.contains(writer)) return;
        writers.add(writer);
    }

    public void removeWriter(String writer) {
        if (!writers.contains(writer)) return;
        if (topics.stream().map(Topic::getPublicArticles).flatMap(List::stream)
                .filter(a -> a.getWriter().equals(writer)).count() > 1) return;
        writers.remove(writer);
    }

    public void confirmPolicyManaged() {
        if (policy == MANAGED) return;
        throw new IllegalStateException("Not Managed Magazine");
    }

    public void applyMember(String current) {
        confirmPolicyManaged();
        if (members.contains(current)) return;

        registerEvent(new MemberAppliedEvent(id, title, manager, current));
    }

    public void quitMember(String current) {
        confirmPolicyManaged();
        if (!members.contains(current)) return;

        members.remove(current);
        registerEvent(new MemberQuitEvent(id, title, manager, current));
    }

    public void addMember(String current, String member) {
        confirmManager(current);
        confirmPolicyManaged();
        if (members.contains(member)) return;

        members.add(member);
        registerEvent(new MemberAddedEvent(id, title, member));
    }

    public void removeMember(String current, String target) {
        confirmManager(current);
        confirmPolicyManaged();
        if (!members.contains(target)) return;

        members.remove(target);
        registerEvent(new MemberRemovedEvent(id, title, target));
    }

    public List<TopicSummary> getTopicSummaries() {
        return topics.stream()
                .sorted((t, o) -> o.getRating() - t.getRating())
                .map(t -> new TopicSummary(t.getId(), t.getTitle()))
                .collect(Collectors.toList());
    }

}
