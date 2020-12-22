package me.scene.dinner.integration.service.board.magazine;

import me.scene.dinner.board.magazine.application.cache.BestMagazineCache;
import me.scene.dinner.board.magazine.application.command.MagazineService;
import me.scene.dinner.board.magazine.application.command.request.MagazineCreateRequest;
import me.scene.dinner.board.magazine.application.command.request.MagazineUpdateRequest;
import me.scene.dinner.board.magazine.application.command.event.MagazineDatabaseChangedEvent;
import me.scene.dinner.board.magazine.domain.magazine.model.Type;
import me.scene.dinner.integration.utils.MagazineTestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;


@SpringBootTest
@DisplayName("Magazine_service")
class MagazineServiceTest {

    @Autowired MagazineService service;

    @MockBean BestMagazineCache cache;

    @Autowired MagazineTestHelper helper;


    @AfterEach
    void clear() {
        helper.clearMagazines();
    }


    @Nested class OnCreate {
        @Test
        void publishes() {
            MagazineCreateRequest request = new MagazineCreateRequest("owner", "title", "short", "long", "OPEN");
            Long id = service.save(request);

            MagazineDatabaseChangedEvent event = new MagazineDatabaseChangedEvent(id);
            then(cache).should().updateByEvent(event);
        }
    }

    @Nested class OnUpdate {
        @Test
        void publishes() {
            Long id = helper.createMagazine("owner", "title", Type.OPEN);

            MagazineUpdateRequest request = new MagazineUpdateRequest("owner", "new", "short", "long");
            service.update(id, request);

            MagazineDatabaseChangedEvent event = new MagazineDatabaseChangedEvent(id);
            then(cache).should(atLeastOnce()).updateByEvent(event);
        }
    }

    @Nested class OnDelete {
        @Test
        void publishes() {
            Long id = helper.createMagazine("owner", "title", Type.OPEN);

            service.delete(id, "owner");

            MagazineDatabaseChangedEvent event = new MagazineDatabaseChangedEvent(id);
            then(cache).should(atLeastOnce()).updateByEvent(event);
        }
    }

}
