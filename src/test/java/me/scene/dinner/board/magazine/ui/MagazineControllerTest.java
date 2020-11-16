package me.scene.dinner.board.magazine.ui;

import me.scene.dinner.account.domain.Account;
import me.scene.dinner.account.domain.AccountRepository;
import me.scene.dinner.board.magazine.application.MagazineService;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.magazine.domain.MagazineRepository;
import me.scene.dinner.board.magazine.domain.Policy;
import me.scene.dinner.common.exception.BoardNotFoundException;
import me.scene.dinner.utils.authentication.WithAccount;
import me.scene.dinner.utils.factory.AccountFactory;
import me.scene.dinner.utils.factory.TopicFactory;
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
class MagazineControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountFactory accountFactory;
    @Autowired AccountRepository accountRepository;
    @Autowired MagazineService magazineService;
    @Autowired MagazineRepository magazineRepository;
    @Autowired TopicFactory topicFactory;

    @Test
    @WithAccount(username = "scene")
    void createPage_hasForm() throws Exception {
        mockMvc.perform(
                get("/magazine-form")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/board/magazine/form"))
                .andExpect(model().attributeExists("magazineForm"))
        ;
    }

    @Test
    void createPage_unauthenticated_beGuidedBySpringSecurity() throws Exception {
        mockMvc.perform(
                get("/magazine-form")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void create_saveAndShow() throws Exception {
        mockMvc.perform(
                post("/magazines")
                        .with(csrf())
                        .param("title", "Test Magazine")
                        .param("shortExplanation", "This is short explanation.")
                        .param("longExplanation", "This is long explanation of test magazine.")
                        .param("policy", "OPEN")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/magazines/*"))
        ;
        Magazine magazine = magazineRepository.findByTitle("Test Magazine").orElseThrow();
        assertThat(magazine.getShortExplanation()).isEqualTo("This is short explanation.");
        assertThat(magazine.getLongExplanation()).isEqualTo("This is long explanation of test magazine.");
        assertThat(magazine.getPolicy()).isEqualTo(Policy.OPEN);
        assertThat(magazine.getManager()).isEqualTo(accountRepository.findByUsername("scene").orElseThrow().getUsername());
    }

    @Test
    void create_unauthenticated_beGuidedBySpringSecurity() throws Exception {
        mockMvc.perform(
                post("/magazines").with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void create_invalidParam_returnErrors() throws Exception {
        mockMvc.perform(
                post("/magazines")
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/board/magazine/form"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(4))
        ;
    }

    @Test
    void show_hasMagazine() throws Exception {
        Account account = accountFactory.create("scene", "scene@email.com", "password");
        Long id = magazineService.save(account.getUsername(), "title", "short", "long", "OPEN");

        mockMvc.perform(
                get("/magazines/" + id)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/board/magazine/view"))
                .andExpect(model().attributeExists("magazine"))
        ;
    }

    @Test
    void show_nonExistent_handleException() throws Exception {
        Account account = accountFactory.create("scene", "scene@email.com", "password");
        Long id = magazineService.save(account.getUsername(), "title", "short", "long", "OPEN");

        mockMvc.perform(
                get("/magazines/" + (id + 1))
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/board_not_found"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void updatePage_hasForm() throws Exception {
        Long id = magazineService.save("scene", "title", "short", "long", "OPEN");

        mockMvc.perform(
                get("/magazines/" + id + "/form")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/board/magazine/update"))
                .andExpect(model().attributeExists("updateForm"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void updatePage_byStranger_handleException() throws Exception {
        Account account = accountFactory.create("magazineManager", "manager@email.com", "password");
        Long id = magazineService.save(account.getUsername(), "title", "short", "long", "OPEN");

        mockMvc.perform(
                get("/magazines/" + id + "/form")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/access"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void update_updated() throws Exception {
        Long id = magazineService.save("scene", "title", "short", "long", "OPEN");

        mockMvc.perform(
                put("/magazines/" + id)
                        .with(csrf())
                        .param("id", id.toString())
                        .param("title", "Updated")
                        .param("shortExplanation", "Updated short.")
                        .param("longExplanation", "Updated long.")
                        .param("policy", "OPEN")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/magazines/" + id))
        ;

        Magazine magazine = magazineService.find(id);
        assertThat(magazine.getTitle()).isEqualTo("Updated");
        assertThat(magazine.getShortExplanation()).isEqualTo("Updated short.");
        assertThat(magazine.getLongExplanation()).isEqualTo("Updated long.");
    }

    @Test
    @WithAccount(username = "scene")
    void update_byStranger_handleException() throws Exception {
        Account account = accountFactory.create("magazineManager", "manager@email.com", "password");
        Long id = magazineService.save(account.getUsername(), "title", "short", "long", "OPEN");

        mockMvc.perform(
                put("/magazines/" + id)
                        .with(csrf())
                        .param("id", id.toString())
                        .param("title", "Updated")
                        .param("shortExplanation", "Updated short.")
                        .param("longExplanation", "Updated long.")
                        .param("policy", "OPEN")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/access"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void update_invalidParam_redirected() throws Exception {
        Long id = magazineService.save("scene", "title", "short", "long", "OPEN");

        mockMvc.perform(
                put("/magazines/" + id)
                        .with(csrf())
                        .param("id", "")
                        .param("title", "")
                        .param("shortExplanation", "")
                        .param("longExplanation", "")
                        .param("policy", "")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/magazines/" + id + "/form"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void delete_deleted() throws Exception {
        Long id = magazineService.save("scene", "title", "short", "long", "OPEN");

        mockMvc.perform(
                delete("/magazines/" + id)
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
        ;
        assertThrows(BoardNotFoundException.class, () -> magazineService.find(id));
    }

    @Test
    @WithAccount(username = "scene")
    void delete_byStranger_handleException() throws Exception {
        Account account = accountFactory.create("magazineManager", "manager@email.com", "password");
        Long id = magazineService.save(account.getUsername(), "title", "short", "long", "OPEN");

        mockMvc.perform(
                delete("/magazines/" + id)
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/access"))
        ;
        assertDoesNotThrow(() -> magazineService.find(id));
    }

    @Test
    @WithAccount(username = "scene")
    void delete_hasChild_handleException() throws Exception {
        Long id = magazineService.save("scene", "title", "short", "long", "OPEN");
        topicFactory.create(id, "scene", "title", "short", "long");

        mockMvc.perform(
                delete("/magazines/" + id)
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/not_deletable"))
        ;
        assertDoesNotThrow(() -> magazineService.find(id));
    }

}
