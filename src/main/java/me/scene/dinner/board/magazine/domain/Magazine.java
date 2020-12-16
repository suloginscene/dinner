package me.scene.dinner.board.magazine.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.board.common.Board;
import me.scene.dinner.board.common.NotDeletableException;
import me.scene.dinner.board.common.Owner;
import me.scene.dinner.board.magazine.domain.event.MemberAddedEvent;
import me.scene.dinner.board.magazine.domain.event.MemberAppliedEvent;
import me.scene.dinner.board.magazine.domain.event.MemberQuitEvent;
import me.scene.dinner.board.magazine.domain.event.MemberRemovedEvent;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static me.scene.dinner.board.magazine.domain.Policy.EXCLUSIVE;
import static me.scene.dinner.board.magazine.domain.Policy.MANAGED;
import static me.scene.dinner.board.magazine.domain.Policy.OPEN;

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

    private int topicCount;

    @OneToMany(cascade = ALL, orphanRemoval = true) @JoinColumn(name = "magazine_id")
    private final List<Writer> writers = new ArrayList<>();

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
        if (topicCount != 0) throw new NotDeletableException(title);
    }


    public void addTopic() {
        topicCount++;
    }

    public void removeTopic() {
        topicCount--;
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

    public void logWritingBy(String writerName) {
        findWriterByName(writerName).ifPresentOrElse(Writer::write, () -> writers.add(new Writer(writerName)));
    }

    public void logErasingBy(String writerName) {
        findWriterByName(writerName).ifPresent(writer -> {
            writer.erase();
            if (writer.getWriteCount() == 0) {
                writers.remove(writer);
            }
        });
    }

    private Optional<Writer> findWriterByName(String writerName) {
        return writers.stream().filter(w -> w.getWriterName().equals(writerName)).findAny();
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

}
