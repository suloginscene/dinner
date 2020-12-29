package me.scene.dinner.board.magazine.domain.managed.model;

import lombok.NoArgsConstructor;
import me.scene.dinner.board.magazine.domain.magazine.model.Authorization;
import me.scene.dinner.board.magazine.domain.magazine.model.Magazine;
import me.scene.dinner.board.magazine.domain.magazine.model.Type;

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
import static me.scene.dinner.board.magazine.domain.magazine.model.Type.MANAGED;


@Entity
@NoArgsConstructor(access = PROTECTED)
public class ManagedMagazine extends Magazine {

    @Transient
    private final Authorization authorization = new Authorization(username -> owner.is(username) || findMember(username).isPresent());

    @OneToMany(cascade = ALL, orphanRemoval = true) @JoinColumn(name = "magazine_id")
    private final Set<Member> members = new HashSet<>();


    public ManagedMagazine(String owner, String title, String shortExplanation, String longExplanation) {
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


    public boolean apply(String memberName) {
        Optional<Member> optionalMember = findMember(memberName);
        return optionalMember.isEmpty();
    }

    public boolean quit(String memberName) {
        Optional<Member> optionalMember = findMember(memberName);
        if (optionalMember.isEmpty()) return false;

        Member member = optionalMember.get();
        members.remove(member);
        return true;
    }


    public boolean addMember(String ownerName, String memberName) {
        owner.identify(ownerName);

        if (owner.is(memberName)) return false;

        Optional<Member> optionalMember = findMember(memberName);
        if (optionalMember.isPresent()) return false;

        Member member = new Member(memberName);
        members.add(member);
        return true;
    }

    public boolean removeMember(String ownerName, String memberName) {
        owner.identify(ownerName);

        Optional<Member> optionalMember = findMember(memberName);
        if (optionalMember.isEmpty()) return false;

        Member member = optionalMember.get();
        members.remove(member);
        return true;
    }


    private Optional<Member> findMember(String name) {
        return members.stream()
                .filter(member -> member.is(name))
                .findAny();
    }

}
