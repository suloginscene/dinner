package me.scene.dinner.board.magazine.ui;

import me.scene.dinner.account.domain.Account;
import me.scene.dinner.account.domain.AccountRepository;
import me.scene.dinner.account.utils.AccountFactory;
import me.scene.dinner.account.utils.WithAccount;
import me.scene.dinner.board.magazine.application.MagazineService;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.magazine.domain.MagazinePolicy;
import me.scene.dinner.board.magazine.domain.MagazineRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Test
    @WithAccount(username = "scene")
    void magazineCreatePage_hasForm() throws Exception {
        mockMvc.perform(
                get("/magazine-form")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/board/magazine/form"))
                .andExpect(model().attributeExists("magazineForm"))
        ;
    }

    @Test
    void magazineCreatePage_unauthenticated_beGuidedBySpringSecurity() throws Exception {
        mockMvc.perform(
                get("/magazine-form")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void createMagazine_saveAndShow() throws Exception {
        mockMvc.perform(
                post("/magazines")
                        .param("title", "Test Magazine")
                        .param("shortExplanation", "This is short explanation.")
                        .param("longExplanation", "This is long explanation of test magazine.")
                        .param("magazinePolicy", "OPEN")
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/magazines/*"))
        ;
        Magazine magazine = magazineRepository.findByTitle("Test Magazine").orElseThrow();
        assertThat(magazine.getShortExplanation()).isEqualTo("This is short explanation.");
        assertThat(magazine.getLongExplanation()).isEqualTo("This is long explanation of test magazine.");
        assertThat(magazine.getMagazinePolicy()).isEqualTo(MagazinePolicy.OPEN);
        assertThat(magazine.getManagerId()).isEqualTo(accountRepository.findByUsername("scene").orElseThrow().getId());
    }

    @Test
    void createMagazine_unauthenticated_beGuidedBySpringSecurity() throws Exception {
        mockMvc.perform(
                post("/magazines").with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void createMagazine_invalidParam_returnErrors() throws Exception {
        mockMvc.perform(
                post("/magazines")
                        .with(csrf())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("page/board/magazine/form"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(4))
        ;
    }

    @Test
    void showMagazine_hasMagazineDto() throws Exception {
        Account account = accountFactory.create("scene", "scene@email.com", "password");
        Long id = magazineService.save(account.getId(), "title", "short", "long", "OPEN");

        mockMvc.perform(
                get("/magazines/" + id)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/board/magazine/view"))
                .andExpect(model().attributeExists("magazineDto"))
        ;
    }

}