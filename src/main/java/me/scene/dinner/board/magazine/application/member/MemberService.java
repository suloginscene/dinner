package me.scene.dinner.board.magazine.application.member;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.common.BoardNotFoundException;
import me.scene.dinner.board.magazine.domain.ManagedMagazine;
import me.scene.dinner.board.magazine.domain.common.Magazine;
import me.scene.dinner.board.magazine.domain.common.MagazineRepository;
import org.hibernate.TypeMismatchException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MagazineRepository repository;

    private final ApplicationEventPublisher publisher;


    public List<String> memberNames(Long id) {
        ManagedMagazine magazine = findManagedMagazine(id);
        return magazine.memberNames();
    }


    public void applyMember(Long id, String memberName) {
        ManagedMagazine magazine = findManagedMagazine(id);

        ManagedMagazine.Member member = findMember(magazine, memberName);
        if (member != null) return;

        // to Send Notification to manager
        MemberAppliedEvent event = appliedEvent(magazine, memberName);
        publisher.publishEvent(event);
    }

    public void quitMember(Long id, String memberName) {
        ManagedMagazine magazine = findManagedMagazine(id);

        ManagedMagazine.Member member = findMember(magazine, memberName);
        if (member == null) return;

        magazine.quitMember(member);

        // to Send Notification to manager
        MemberQuitEvent event = quitEvent(magazine, memberName);
        publisher.publishEvent(event);
    }


    public void addMember(Long id, String ownerName, String memberName) {
        ManagedMagazine magazine = findManagedMagazine(id);

        ManagedMagazine.Member member = findMember(magazine, memberName);
        if (member != null) return;

        ManagedMagazine.Member newMember = new ManagedMagazine.Member(memberName);
        magazine.addMember(ownerName, newMember);

        // to Send Notification to target
        MemberAddedEvent event = addedEvent(magazine, memberName);
        publisher.publishEvent(event);
    }

    public void removeMember(Long id, String ownerName, String memberName) {
        ManagedMagazine magazine = findManagedMagazine(id);

        ManagedMagazine.Member member = findMember(magazine, memberName);
        if (member == null) return;

        magazine.removeMember(ownerName, member);

        // to Send Notification to target
        MemberRemovedEvent event = removedEvent(magazine, memberName);
        publisher.publishEvent(event);
    }


    // private ---------------------------------------------------------------------------------------------------------

    private ManagedMagazine findManagedMagazine(Long id) {
        Magazine magazine = repository.findById(id).orElseThrow(() -> new BoardNotFoundException(id));
        if (magazine instanceof ManagedMagazine) {
            return (ManagedMagazine) magazine;
        }
        throw new TypeMismatchException(magazine.getTitle() + " is not MANAGED");
    }

    private ManagedMagazine.Member findMember(ManagedMagazine magazine, String memberName) {
        return magazine.findMemberByName(memberName).orElse(null);
    }


    private MemberAppliedEvent appliedEvent(Magazine m, String applicant) {
        return new MemberAppliedEvent(m.getId(), m.getTitle(), m.getOwner().getOwnerName(), applicant);
    }

    private MemberQuitEvent quitEvent(Magazine m, String member) {
        return new MemberQuitEvent(m.getId(), m.getTitle(), m.getOwner().getOwnerName(), member);
    }

    private MemberAddedEvent addedEvent(Magazine m, String target) {
        return new MemberAddedEvent(m.getId(), m.getTitle(), target);
    }

    private MemberRemovedEvent removedEvent(Magazine m, String target) {
        return new MemberRemovedEvent(m.getId(), m.getTitle(), target);
    }

}
