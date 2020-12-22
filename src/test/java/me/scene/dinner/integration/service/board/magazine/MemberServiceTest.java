package me.scene.dinner.integration.service.board.magazine;

import me.scene.dinner.board.magazine.application.command.MemberService;
import me.scene.dinner.board.magazine.application.command.event.MemberAddedEvent;
import me.scene.dinner.board.magazine.application.command.event.MemberAppliedEvent;
import me.scene.dinner.board.magazine.application.command.event.MemberQuitEvent;
import me.scene.dinner.board.magazine.application.command.event.MemberRemovedEvent;
import me.scene.dinner.board.magazine.domain.magazine.repository.MagazineRepository;
import me.scene.dinner.board.magazine.domain.magazine.model.Type;
import me.scene.dinner.board.magazine.domain.managed.model.ManagedMagazine;
import me.scene.dinner.integration.utils.MagazineTestHelper;
import me.scene.dinner.account.application.command.notification.NotificationListener;
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
            MemberAppliedEvent event = new MemberAppliedEvent(magazine, "member");
            then(notification).should().notify(event);
        }
    }

    @Nested class OnAdd {
        @Test
        void publishes() {
            service.addMember(id, "owner", "member");

            ManagedMagazine magazine = (ManagedMagazine) repository.find(id);
            MemberAddedEvent event = new MemberAddedEvent(magazine, "member");
            then(notification).should().notify(event);
        }
    }

    @Nested class OnQuit {
        @Test
        void publishes() {
            service.addMember(id, "owner", "member");

            service.quitMember(id, "member");

            ManagedMagazine magazine = (ManagedMagazine) repository.find(id);
            MemberQuitEvent event = new MemberQuitEvent(magazine, "member");
            then(notification).should().notify(event);
        }
    }

    @Nested class OnRemove {
        @Test
        void publishes() {
            service.addMember(id, "owner", "member");

            service.removeMember(id, "owner", "member");

            ManagedMagazine magazine = (ManagedMagazine) repository.find(id);
            MemberRemovedEvent event = new MemberRemovedEvent(magazine, "member");
            then(notification).should().notify(event);
        }
    }

}
