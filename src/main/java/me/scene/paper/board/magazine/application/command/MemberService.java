package me.scene.paper.board.magazine.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.paper.board.magazine.domain.magazine.model.Magazine;
import me.scene.paper.board.magazine.domain.magazine.repository.MagazineRepository;
import me.scene.paper.board.magazine.domain.managed.model.ManagedMagazine;
import me.scene.paper.common.notification.message.NotificationMessageFactory;
import me.scene.paper.common.notification.event.NotificationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static me.scene.paper.board.magazine.domain.magazine.model.Type.MANAGED;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MagazineRepository repository;

    private final NotificationMessageFactory messageFactory;
    private final NotificationEventPublisher notification;


    public List<String> memberNames(Long id) {
        ManagedMagazine magazine = findManagedMagazine(id);
        return magazine.memberNames();
    }


    public void applyMember(Long id, String memberName) {
        ManagedMagazine magazine = findManagedMagazine(id);
        boolean success = magazine.apply(memberName);

        if (success) {
            String receiver = magazine.getOwner().name();
            String message = messageFactory.memberApplied(memberName, magazine.getId(), magazine.getTitle());
            notification.publish(receiver, message);
        }
    }

    public void quitMember(Long id, String memberName) {
        ManagedMagazine magazine = findManagedMagazine(id);
        boolean success = magazine.quit(memberName);

        if (success) {
            String receiver = magazine.getOwner().name();
            String message = messageFactory.memberQuit(memberName, magazine.getId(), magazine.getTitle());
            notification.publish(receiver, message);
        }
    }


    public void addMember(Long id, String ownerName, String memberName) {
        ManagedMagazine magazine = findManagedMagazine(id);
        boolean success = magazine.addMember(ownerName, memberName);

        if (success) {
            String message = messageFactory.memberAdded(magazine.getId(), magazine.getTitle());
            notification.publish(memberName, message);
        }
    }

    public void removeMember(Long id, String ownerName, String memberName) {
        ManagedMagazine magazine = findManagedMagazine(id);
        boolean success = magazine.removeMember(ownerName, memberName);

        if (success) {
            String message = messageFactory.memberRemoved(magazine.getId(), magazine.getTitle());
            notification.publish(memberName, message);
        }
    }


    private ManagedMagazine findManagedMagazine(Long id) {
        Magazine magazine = repository.find(id);
        magazine.type().check(MANAGED);
        return (ManagedMagazine) magazine;
    }

}
