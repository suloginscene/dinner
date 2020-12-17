package me.scene.dinner.board.magazine.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.board.common.Owner;
import me.scene.dinner.board.magazine.domain.common.AccessException;
import me.scene.dinner.board.magazine.domain.common.Magazine;
import me.scene.dinner.common.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.ALL;
import static lombok.AccessLevel.PROTECTED;
import static me.scene.dinner.board.magazine.domain.common.Type.MANAGED;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class ManagedMagazine extends Magazine {

    @OneToMany(cascade = ALL, orphanRemoval = true) @JoinColumn(name = "magazine_id")
    private final Set<Member> members = new HashSet<>();


    public ManagedMagazine(Owner owner, String title, String shortExplanation, String longExplanation) {
        super(owner, title, shortExplanation, longExplanation);
    }


    @Override
    public String type() {
        return MANAGED.name();
    }

    @Override
    public List<String> memberNames() {
        return members.stream()
                .map(Member::getName)
                .collect(toList());
    }

    @Override
    public void checkAuthorization(String username) {
        if (owner.is(username)) return;

        Optional<Member> member = findMemberByName(username);
        if (member.isPresent()) return;

        throw new AccessException(username, getTitle(), MANAGED);
    }


    public Optional<Member> findMemberByName(String name) {
        return members.stream()
                .filter(member -> member.is(name))
                .findAny();
    }

    public void quitMember(Member member) {
        members.remove(member);
    }

    public void addMember(String ownerName, Member member) {
        owner.identify(ownerName);
        members.add(member);
    }

    public void removeMember(String ownerName, Member member) {
        owner.identify(ownerName);
        members.remove(member);
    }


    @Entity @NoArgsConstructor(access = PROTECTED)
    public static class Member extends BaseEntity {

        @Getter
        private String name;

        public Member(String name) {
            this.name = name;
        }

        private boolean is(String username) {
            return name.equals(username);
        }

    }

}
