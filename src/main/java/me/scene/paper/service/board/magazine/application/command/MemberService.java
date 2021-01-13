package me.scene.paper.service.board.magazine.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.paper.service.board.magazine.domain.magazine.model.Magazine;
import me.scene.paper.service.board.magazine.domain.magazine.repository.MagazineRepository;
import me.scene.paper.common.communication.notification.publisher.NotificationEventPublisher;
import me.scene.paper.common.utility.NotificationMessageFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MagazineRepository repository;

    private final NotificationMessageFactory messageFactory;
    private final NotificationEventPublisher notification;


    public void applyMember(Long id, String memberName) {
        Magazine magazine = repository.find(id);
        boolean success = magazine.actAsMember(memberName, true);

        if (success) {
            String receiver = magazine.getOwnerName();
            String message = messageFactory.memberApplied(memberName, magazine.getId(), magazine.getTitle());
            notification.publish(receiver, message);
        }
    }

    public void quitMember(Long id, String memberName) {
        Magazine magazine = repository.find(id);
        boolean success = magazine.actAsMember(memberName, false);

        if (success) {
            String receiver = magazine.getOwnerName();
            String message = messageFactory.memberQuit(memberName, magazine.getId(), magazine.getTitle());
            notification.publish(receiver, message);
        }
    }


    public void addMember(Long id, String ownerName, String memberName) {
        Magazine magazine = repository.find(id);
        boolean success = magazine.manageMember(ownerName, memberName, true);

        if (success) {
            String message = messageFactory.memberAdded(magazine.getId(), magazine.getTitle());
            notification.publish(memberName, message);
        }
    }

    public void removeMember(Long id, String ownerName, String memberName) {
        Magazine magazine = repository.find(id);
        boolean success = magazine.manageMember(ownerName, memberName, false);

        if (success) {
            String message = messageFactory.memberRemoved(magazine.getId(), magazine.getTitle());
            notification.publish(memberName, message);
        }
    }

}
