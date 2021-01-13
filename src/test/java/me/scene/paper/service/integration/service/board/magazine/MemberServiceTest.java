package me.scene.paper.service.integration.service.board.magazine;

import me.scene.paper.service.account.application.listener.NotificationListener;
import me.scene.paper.service.board.magazine.application.command.MemberService;
import me.scene.paper.service.board.magazine.domain.magazine.model.Type;
import me.scene.paper.service.board.magazine.domain.magazine.repository.MagazineRepository;
import me.scene.paper.service.board.magazine.domain.managed.model.ManagedMagazine;
import me.scene.paper.common.communication.notification.event.NotificationEvent;
import me.scene.paper.common.utility.NotificationMessageFactory;
import me.scene.paper.service.integration.utils.MagazineTestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.BDDMockito.then;


@SpringBootTest
@DisplayName("Magazine_service(member)")
class MemberServiceTest {

    @Autowired MemberService service;

    @Autowired MagazineRepository repository;

    @Autowired NotificationMessageFactory messageFactory;
    @MockBean NotificationListener notification;

    @Autowired MagazineTestHelper helper;


    Long id;

    @BeforeEach
    void create() {
        id = helper.createMagazine("owner", "title", Type.MANAGED);
    }

    @AfterEach
    void clear() {
        helper.clearMagazines();
    }


    @Nested class OnApply {
        @Test
        void publishes() {
            service.applyMember(id, "member");

            ManagedMagazine magazine = (ManagedMagazine) repository.find(id);
            String receiver = magazine.getOwnerName();
            String message = messageFactory.memberApplied("member", magazine.getId(), magazine.getTitle());
            NotificationEvent event = new NotificationEvent(receiver, message);
            then(notification).should().notify(event);
        }
    }

    @Nested class OnAdd {
        @Test
        void publishes() {
            service.addMember(id, "owner", "member");

            ManagedMagazine magazine = (ManagedMagazine) repository.find(id);
            String message = messageFactory.memberAdded(magazine.getId(), magazine.getTitle());
            NotificationEvent event = new NotificationEvent("member", message);
            then(notification).should().notify(event);
        }
    }

    @Nested class OnQuit {
        @Test
        void publishes() {
            service.addMember(id, "owner", "member");

            service.quitMember(id, "member");

            ManagedMagazine magazine = (ManagedMagazine) repository.find(id);
            String receiver = magazine.getOwnerName();
            String message = messageFactory.memberQuit("member", magazine.getId(), magazine.getTitle());
            NotificationEvent event = new NotificationEvent(receiver, message);
            then(notification).should().notify(event);
        }
    }

    @Nested class OnRemove {
        @Test
        void publishes() {
            service.addMember(id, "owner", "member");

            service.removeMember(id, "owner", "member");

            ManagedMagazine magazine = (ManagedMagazine) repository.find(id);
            String message = messageFactory.memberRemoved(magazine.getId(), magazine.getTitle());
            NotificationEvent event = new NotificationEvent("member", message);
            then(notification).should().notify(event);
        }
    }

}
