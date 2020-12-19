package me.scene.dinner.board.magazine.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.board.common.Owner;
import me.scene.dinner.board.magazine.domain.common.Magazine;
import me.scene.dinner.common.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.ALL;
import static lombok.AccessLevel.PROTECTED;


@Entity
@NoArgsConstructor(access = PROTECTED)
public class ManagedMagazine extends Magazine {

    @OneToMany(cascade = ALL, orphanRemoval = true) @JoinColumn(name = "magazine_id")
    private final Set<Member> members = new HashSet<>();


    public ManagedMagazine(Owner owner, String title, String shortExplanation, String longExplanation) {
        super(owner, title, shortExplanation, longExplanation);
    }


    @Override
    public Type type() {
        return Type.MANAGED;
    }

    @Override
    protected Predicate<String> authorize() {
        return username -> {
            if (owner.is(username)) return true;
            return findMember(username).isPresent();
        };
    }


    @Override
    public List<String> memberNames() {
        return members.stream()
                .map(Member::getName)
                .collect(toList());
    }


    private Optional<Member> findMember(String name) {
        return members.stream()
                .filter(member -> member.is(name))
                .findAny();
    }


    public Optional<Member.ManagerEvent> apply(String memberName) {
        Optional<Member> optionalMember = findMember(memberName);
        if (optionalMember.isPresent()) return Optional.empty();

        return Optional.of(new Member.ManagerEvent(this, memberName, Member.ManagerEvent.Status.APPLIED));
    }

    public Optional<Member.ManagerEvent> quit(String memberName) {
        Optional<Member> optionalMember = findMember(memberName);
        if (optionalMember.isEmpty()) return Optional.empty();

        Member member = optionalMember.get();
        members.remove(member);

        return Optional.of(new Member.ManagerEvent(this, memberName, Member.ManagerEvent.Status.QUIT));
    }

    public Optional<Member.MemberEvent> addMember(String ownerName, String memberName) {
        owner.identify(ownerName);

        Optional<Member> optionalMember = findMember(memberName);
        if (optionalMember.isPresent()) return Optional.empty();

        Member member = new Member(memberName);
        members.add(member);

        return Optional.of(new Member.MemberEvent(this, memberName, Member.MemberEvent.Status.ADDED));
    }

    public Optional<Member.MemberEvent> removeMember(String ownerName, String memberName) {
        owner.identify(ownerName);

        Optional<Member> optionalMember = findMember(memberName);
        if (optionalMember.isEmpty()) return Optional.empty();

        Member member = optionalMember.get();
        members.remove(member);

        return Optional.of(new Member.MemberEvent(this, memberName, Member.MemberEvent.Status.REMOVED));
    }


    @Entity @NoArgsConstructor(access = PROTECTED)
    public static class Member extends BaseEntity {

        @Getter
        private String name;

        private Member(String name) {
            this.name = name;
        }

        private boolean is(String username) {
            return name.equals(username);
        }


        @Data
        abstract static class Event {
            private final Long magazineId;
            private final String magazineTitle;
            private final String memberName;

            protected Event(ManagedMagazine m, String memberName) {
                magazineId = m.getId();
                magazineTitle = m.getTitle();
                this.memberName = memberName;
            }
        }

        public static class ManagerEvent extends Event {
            public enum Status {APPLIED, QUIT}

            @Getter private final String managerName;
            @Getter private final Status status;

            private ManagerEvent(ManagedMagazine magazine, String memberName, Status status) {
                super(magazine, memberName);
                managerName = magazine.getOwner().getOwnerName();
                this.status = status;
            }
        }

        public static class MemberEvent extends Event {
            public enum Status {ADDED, REMOVED}

            @Getter private final Status status;

            private MemberEvent(ManagedMagazine magazine, String memberName, Status status) {
                super(magazine, memberName);
                this.status = status;
            }
        }

    }

}
