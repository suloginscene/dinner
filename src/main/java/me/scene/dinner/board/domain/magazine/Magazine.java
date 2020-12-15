package me.scene.dinner.board.domain.magazine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.board.domain.common.Board;
import me.scene.dinner.board.domain.common.NotDeletableException;
import me.scene.dinner.board.domain.common.Owner;
import me.scene.dinner.board.domain.magazine.event.MemberAddedEvent;
import me.scene.dinner.board.domain.magazine.event.MemberAppliedEvent;
import me.scene.dinner.board.domain.magazine.event.MemberQuitEvent;
import me.scene.dinner.board.domain.magazine.event.MemberRemovedEvent;
import me.scene.dinner.board.domain.topic.Topic;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static me.scene.dinner.board.domain.magazine.Policy.EXCLUSIVE;
import static me.scene.dinner.board.domain.magazine.Policy.MANAGED;
import static me.scene.dinner.board.domain.magazine.Policy.OPEN;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Magazine extends Board {

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


    public Magazine(String owner, String title, String shortExplanation, String longExplanation, String magazinePolicy) {
        this.owner = new Owner(owner);
        this.title = title;
        this.shortExplanation = shortExplanation;
        this.longExplanation = longExplanation;
        this.policy = Policy.valueOf(magazinePolicy);
    }

    public void update(String current, String title, String shortExplanation, String longExplanation) {
        owner.identify(current);
        this.title = title;
        this.shortExplanation = shortExplanation;
        this.longExplanation = longExplanation;
    }

    public void rate(int point) {
        rating += point;
    }

    public void beforeDelete(String current) {
        owner.identify(current);
        confirmDeletable();
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
            if (owner.is(username)) return;
        }

        if (policy == MANAGED) {
            if (owner.is(username)) return;
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
                .filter(a -> a.getOwner().is(writer)).count() > 1) return;
        writers.remove(writer);
    }

    public void confirmPolicyManaged() {
        if (policy == MANAGED) return;
        throw new IllegalStateException("Not Managed Magazine");
    }

    public Optional<MemberAppliedEvent> applyMember(String current) {
        confirmPolicyManaged();
        if (members.contains(current)) return Optional.empty();

        return Optional.of(new MemberAppliedEvent(id, title, owner.getOwnerName(), current));
    }

    public Optional<MemberQuitEvent> quitMember(String current) {
        confirmPolicyManaged();
        if (!members.contains(current)) return Optional.empty();

        members.remove(current);
        return Optional.of(new MemberQuitEvent(id, title, owner.getOwnerName(), current));
    }

    public Optional<MemberAddedEvent> addMember(String current, String member) {
        owner.identify(current);
        confirmPolicyManaged();
        if (members.contains(member)) return Optional.empty();

        members.add(member);
        return Optional.of(new MemberAddedEvent(id, title, member));
    }

    public Optional<MemberRemovedEvent> removeMember(String current, String target) {
        owner.identify(current);
        confirmPolicyManaged();
        if (!members.contains(target)) return Optional.empty();

        members.remove(target);
        return Optional.of(new MemberRemovedEvent(id, title, target));
    }

    public List<TopicSummary> getTopicSummaries() {
        return topics.stream()
                .sorted((t, o) -> o.getRating() - t.getRating())
                .map(t -> new TopicSummary(t.getId(), t.getTitle()))
                .collect(Collectors.toList());
    }

}
