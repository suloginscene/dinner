package me.scene.dinner.integration.service;

import me.scene.dinner.board.magazine.application.command.MemberService;
import me.scene.dinner.board.magazine.domain.common.MagazineRepository;
import me.scene.dinner.board.magazine.domain.common.Type;
import me.scene.dinner.board.magazine.domain.managed.ManagedEvent;
import me.scene.dinner.board.magazine.domain.managed.ManagedMagazine;
import me.scene.dinner.board.magazine.domain.managed.MemberAppliedEvent;
import me.scene.dinner.board.magazine.domain.managed.MemberQuitEvent;
import me.scene.dinner.integration.utils.MagazineTestHelper;
import me.scene.dinner.notification.application.NotificationListener;
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
            then(notification).should().onMemberApplyMagazine(event);
        }
    }

    @Nested class OnAdd {
        @Test
        void publishes() {
            service.addMember(id, "owner", "member");

            ManagedMagazine magazine = (ManagedMagazine) repository.find(id);
            ManagedEvent event = new ManagedEvent(magazine, "member", ManagedEvent.Action.ADD);
            then(notification).should().onManagerManageMember(event);
        }
    }

    @Nested class OnQuit {
        @Test
        void publishes() {
            service.addMember(id, "owner", "member");

            service.quitMember(id, "member");

            ManagedMagazine magazine = (ManagedMagazine) repository.find(id);
            MemberQuitEvent event = new MemberQuitEvent(magazine, "member");
            then(notification).should().onMemberQuitMagazine(event);
        }
    }

    @Nested class OnRemove {
        @Test
        void publishes() {
            service.addMember(id, "owner", "member");

            service.removeMember(id, "owner", "member");

            ManagedMagazine magazine = (ManagedMagazine) repository.find(id);
            ManagedEvent event = new ManagedEvent(magazine, "member", ManagedEvent.Action.REMOVE);
            then(notification).should().onManagerManageMember(event);
        }
    }

}
