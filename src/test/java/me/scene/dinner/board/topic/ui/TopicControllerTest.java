package me.scene.dinner.board.topic.ui;

import me.scene.dinner.account.domain.Account;
import me.scene.dinner.account.domain.AccountRepository;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.topic.application.TopicService;
import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.board.topic.domain.TopicRepository;
import me.scene.dinner.common.exception.BoardNotFoundException;
import me.scene.dinner.utils.authentication.WithAccount;
import me.scene.dinner.utils.factory.AccountFactory;
import me.scene.dinner.utils.factory.ArticleFactory;
import me.scene.dinner.utils.factory.MagazineFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class TopicControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountFactory accountFactory;
    @Autowired AccountRepository accountRepository;
    @Autowired MagazineFactory magazineFactory;
    @Autowired TopicService topicService;
    @Autowired TopicRepository topicRepository;
    @Autowired ArticleFactory articleFactory;

    @Test
    @WithAccount(username = "scene")
    void createPage_hasForm() throws Exception {
        mockMvc.perform(
                get("/topic-form")
                        .param("magazineId", "1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/board/topic/form"))
                .andExpect(model().attributeExists("topicForm"))
        ;
    }

    @Test
    void createPage_unauthenticated_beGuidedBySpringSecurity() throws Exception {
        mockMvc.perform(
                get("/topic-form")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void create_saveAndShow() throws Exception {
        Account account = accountFactory.create("magazineManager", "magazine_manager@email.com", "password");
        Magazine magazine = magazineFactory.create(account.getUsername(), "title", "short", "long", "OPEN");

        mockMvc.perform(
                post("/topics")
                        .param("magazineId", magazine.getId().toString())
                        .param("title", "Test Topic")
                        .param("shortExplanation", "This is short explanation.")
                        .param("longExplanation", "This is long explanation of test magazine.")
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/topics/*"))
        ;
        Topic topic = topicRepository.findByTitle("Test Topic").orElseThrow();
        assertThat(topic.getShortExplanation()).isEqualTo("This is short explanation.");
        assertThat(topic.getLongExplanation()).isEqualTo("This is long explanation of test magazine.");
        assertThat(topic.getMagazine()).isEqualTo(magazine);
        assertThat(topic.getManager()).isEqualTo(accountRepository.findByUsername("scene").orElseThrow().getUsername());
    }

    @Test
    @WithAccount(username = "scene")
    void create_invalidParam_returnErrors() throws Exception {
        mockMvc.perform(
                post("/topics")
                        .with(csrf())
                        .param("magazineId", "1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/board/topic/form"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(3))
        ;
    }

    @Test
    void create_unauthenticated_beGuidedBySpringSecurity() throws Exception {
        mockMvc.perform(
                post("/topics").with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void create_unauthorized_handleException() throws Exception {
        Account account = accountFactory.create("magazineManager", "magazine_manager@email.com", "password");

        Magazine exclusive = magazineFactory.create(account.getUsername(), "title", "short", "long", "EXCLUSIVE");
        mockMvc.perform(
                post("/topics")
                        .param("magazineId", exclusive.getId().toString())
                        .param("title", "Test Topic")
                        .param("shortExplanation", "This is short explanation.")
                        .param("longExplanation", "This is long explanation of test magazine.")
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/access"))
        ;

        Magazine managed = magazineFactory.create(account.getUsername(), "title", "short", "long", "MANAGED");
        mockMvc.perform(
                post("/topics")
                        .param("magazineId", managed.getId().toString())
                        .param("title", "Test Topic")
                        .param("shortExplanation", "This is short explanation.")
                        .param("longExplanation", "This is long explanation of test magazine.")
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/access"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void create_exclusiveByManager_success() throws Exception {
        Account scene = accountRepository.findByUsername("scene").orElseThrow();
        Magazine exclusive = magazineFactory.create(scene.getUsername(), "title", "short", "long", "EXCLUSIVE");

        mockMvc.perform(
                post("/topics")
                        .param("magazineId", exclusive.getId().toString())
                        .param("title", "Test Topic")
                        .param("shortExplanation", "This is short explanation.")
                        .param("longExplanation", "This is long explanation of test magazine.")
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/topics/*"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void create_managedByAuthorized_success() throws Exception {
        Account account = accountFactory.create("magazineManager", "magazine_manager@email.com", "password");
        Magazine managed = magazineFactory.create(account.getUsername(), "title", "short", "long", "MANAGED");
        managed.registerAsAuthorizedWriter("scene");

        mockMvc.perform(
                post("/topics")
                        .param("magazineId", managed.getId().toString())
                        .param("title", "Test Topic")
                        .param("shortExplanation", "This is short explanation.")
                        .param("longExplanation", "This is long explanation of test magazine.")
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/topics/*"))
        ;
    }

    @Test
    void show_hasTopic() throws Exception {
        Account account = accountFactory.create("scene", "scene@email.com", "password");
        Magazine magazine = magazineFactory.create(account.getUsername(), "title", "short", "long", "OPEN");
        Long id = topicService.save(magazine.getId(), account.getUsername(), "title", "short", "long");

        mockMvc.perform(
                get("/topics/" + id)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/board/topic/view"))
                .andExpect(model().attributeExists("topic"))
        ;
    }

    @Test
    void show_nonExistent_handleException() throws Exception {
        Account account = accountFactory.create("scene", "scene@email.com", "password");
        Magazine magazine = magazineFactory.create(account.getUsername(), "title", "short", "long", "OPEN");
        Long id = topicService.save(magazine.getId(), account.getUsername(), "title", "short", "long");

        mockMvc.perform(
                get("/topics/" + (id + 1))
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/board_not_found"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void updatePage_hasForm() throws Exception {
        Magazine magazine = magazineFactory.create("scene", "title", "short", "long", "OPEN");
        Long id = topicService.save(magazine.getId(), "scene", "title", "short", "long");

        mockMvc.perform(
                get("/topics/" + id + "/form")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/board/topic/update"))
                .andExpect(model().attributeExists("updateForm"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void updatePage_byStranger_handleException() throws Exception {
        Account account = accountFactory.create("magazineManager", "manager@email.com", "password");
        Magazine magazine = magazineFactory.create(account.getUsername(), "title", "short", "long", "OPEN");
        Long id = topicService.save(magazine.getId(), account.getUsername(), "title", "short", "long");

        mockMvc.perform(
                get("/topics/" + id + "/form")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/access"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void update_updated() throws Exception {
        Magazine magazine = magazineFactory.create("scene", "title", "short", "long", "OPEN");
        Long id = topicService.save(magazine.getId(), "scene", "title", "short", "long");

        mockMvc.perform(
                put("/topics/" + id)
                        .with(csrf())
                        .param("id", id.toString())
                        .param("magazineId", magazine.getId().toString())
                        .param("title", "Updated")
                        .param("shortExplanation", "Updated short.")
                        .param("longExplanation", "Updated long.")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/topics/" + id))
        ;

        Topic topic = topicService.find(id);
        assertThat(topic.getTitle()).isEqualTo("Updated");
        assertThat(topic.getShortExplanation()).isEqualTo("Updated short.");
        assertThat(topic.getLongExplanation()).isEqualTo("Updated long.");
    }

    @Test
    @WithAccount(username = "scene")
    void update_byStranger_handleException() throws Exception {
        Account account = accountFactory.create("magazineManager", "manager@email.com", "password");
        Magazine magazine = magazineFactory.create(account.getUsername(), "title", "short", "long", "OPEN");
        Long id = topicService.save(magazine.getId(), account.getUsername(), "title", "short", "long");

        mockMvc.perform(
                put("/topics/" + id)
                        .with(csrf())
                        .param("id", id.toString())
                        .param("magazineId", magazine.getId().toString())
                        .param("title", "Updated")
                        .param("shortExplanation", "Updated short.")
                        .param("longExplanation", "Updated long.")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/access"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void update_invalidParam_redirected() throws Exception {
        Magazine magazine = magazineFactory.create("scene", "title", "short", "long", "OPEN");
        Long id = topicService.save(magazine.getId(), "scene", "title", "short", "long");

        mockMvc.perform(
                put("/topics/" + id)
                        .with(csrf())
                        .param("id", "")
                        .param("magazineId", "")
                        .param("title", "")
                        .param("shortExplanation", "")
                        .param("longExplanation", "")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/topics/" + id + "/form"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void delete_deleted() throws Exception {
        Magazine magazine = magazineFactory.create("scene", "title", "short", "long", "OPEN");
        Long id = topicService.save(magazine.getId(), "scene", "title", "short", "long");

        mockMvc.perform(
                delete("/topics/" + id)
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/magazines/" + magazine.getId()))
        ;
        assertThrows(BoardNotFoundException.class, () -> topicService.find(id));
    }

    @Test
    @WithAccount(username = "scene")
    void delete_byStranger_handleException() throws Exception {
        Account account = accountFactory.create("magazineManager", "manager@email.com", "password");
        Magazine magazine = magazineFactory.create(account.getUsername(), "title", "short", "long", "OPEN");
        Long id = topicService.save(magazine.getId(), account.getUsername(), "title", "short", "long");

        mockMvc.perform(
                delete("/topics/" + id)
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/access"))
        ;
        assertDoesNotThrow(() -> topicService.find(id));
    }

    @Test
    @WithAccount(username = "scene")
    void delete_hasChild_handleException() throws Exception {
        Magazine magazine = magazineFactory.create("scene", "title", "short", "long", "OPEN");
        Long id = topicService.save(magazine.getId(), "scene", "title", "short", "long");
        articleFactory.create(id, "scene", "title", "content");

        mockMvc.perform(
                delete("/topics/" + id)
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/not_deletable"))
        ;
        assertDoesNotThrow(() -> topicService.find(id));
    }

}
