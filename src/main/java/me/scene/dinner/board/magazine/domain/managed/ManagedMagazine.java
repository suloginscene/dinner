package me.scene.dinner.board.magazine.domain.managed;

import lombok.NoArgsConstructor;
import me.scene.dinner.board.common.domain.Owner;
import me.scene.dinner.board.magazine.domain.common.Authorization;
import me.scene.dinner.board.magazine.domain.common.Magazine;
import me.scene.dinner.board.magazine.domain.common.Type;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.ALL;
import static lombok.AccessLevel.PROTECTED;
import static me.scene.dinner.board.magazine.domain.common.Type.MANAGED;
import static me.scene.dinner.board.magazine.domain.managed.ManagedEvent.Action.ADD;
import static me.scene.dinner.board.magazine.domain.managed.ManagedEvent.Action.REMOVE;


@Entity
@NoArgsConstructor(access = PROTECTED)
public class ManagedMagazine extends Magazine {

    @Transient
    private final Authorization authorization = new Authorization(username -> owner.is(username) || findMember(username).isPresent());

    @OneToMany(cascade = ALL, orphanRemoval = true) @JoinColumn(name = "magazine_id")
    private final Set<Member> members = new HashSet<>();


    public ManagedMagazine(Owner owner, String title, String shortExplanation, String longExplanation) {
        super(owner, title, shortExplanation, longExplanation);
    }


    @Override
    public Type type() {
        return MANAGED;
    }

    @Override
    public Authorization authorization() {
        return authorization;
    }


    public List<String> memberNames() {
        return members.stream()
                .map(Member::getName)
                .collect(toList());
    }


    public Optional<MemberAppliedEvent> apply(String memberName) {
        Optional<Member> optionalMember = findMember(memberName);
        if (optionalMember.isPresent()) return Optional.empty();

        return Optional.of(new MemberAppliedEvent(this, memberName));
    }

    public Optional<MemberQuitEvent> quit(String memberName) {
        Optional<Member> optionalMember = findMember(memberName);
        if (optionalMember.isEmpty()) return Optional.empty();

        Member member = optionalMember.get();
        members.remove(member);

        return Optional.of(new MemberQuitEvent(this, memberName));
    }


    public Optional<ManagedEvent> addMember(String ownerName, String memberName) {
        owner.identify(ownerName);

        Optional<Member> optionalMember = findMember(memberName);
        if (optionalMember.isPresent()) return Optional.empty();

        Member member = new Member(memberName);
        members.add(member);

        return Optional.of(new ManagedEvent(this, memberName, ADD));
    }

    public Optional<ManagedEvent> removeMember(String ownerName, String memberName) {
        owner.identify(ownerName);

        Optional<Member> optionalMember = findMember(memberName);
        if (optionalMember.isEmpty()) return Optional.empty();

        Member member = optionalMember.get();
        members.remove(member);

        return Optional.of(new ManagedEvent(this, memberName, REMOVE));
    }


    private Optional<Member> findMember(String name) {
        return members.stream()
                .filter(member -> member.is(name))
                .findAny();
    }

}
