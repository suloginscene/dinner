package me.scene.dinner.account.ui;

import me.scene.dinner.account.application.AccountService;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.account.domain.account.Profile;
import me.scene.dinner.test.utils.authentication.WithAccount;
import me.scene.dinner.test.factory.AccountFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class ProfileControllerTest {

    @Autowired MockMvc mockMvc;

    @Autowired AccountService accountService;
    @Autowired PasswordEncoder passwordEncoder;

    @Autowired AccountFactory accountFactory;

    @Autowired AccountRepository accountRepository;

    @AfterEach
    void clear() {
        accountRepository.deleteAll();
    }


    @Test
    void profilePage_hasAccountInfo() throws Exception {
        accountFactory.create("scene", "scene@email.com", "password");

        mockMvc.perform(
                get("/@scene")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/account/profile"))
                .andExpect(model().attributeExists("username", "email"))
        // TODO profile
        ;
    }

    @Test
    void profilePage_nonExistent_handleException() throws Exception {
        mockMvc.perform(
                get("/@scene")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/user_not_found"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void profilePage_isOwner_perceiveOwner() throws Exception {
        mockMvc.perform(
                get("/@scene")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/account/profile"))
        // TODO htmlunit
        ;

        accountFactory.create("another", "another@email.com", "password");
        mockMvc.perform(
                get("/@another")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/account/profile"))
        // TODO htmlunit
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void configPage_hasForms() throws Exception {
        mockMvc.perform(
                get("/config")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/account/config"))
                .andExpect(model().attributeExists("profileForm"))
                .andExpect(model().attributeExists("passwordForm"))
        ;
    }

    @Test
    void configPage_unauthenticated_beGuidedBySpringSecurity() throws Exception {
        mockMvc.perform(
                get("/config")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void configPublic_changed() throws Exception {
        mockMvc.perform(
                post("/public")
                        .with(csrf())
                        .param("shortIntroduction", "short")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/@scene"))
        ;
        Account account = accountService.find("scene");
        Profile profile = account.getProfile();
        assertThat(profile).isEqualTo(new Profile("short"));
    }

    @Test
    @WithAccount(username = "scene")
    void configPublic_invalidParam_returnError() throws Exception {
        mockMvc.perform(
                post("/public")
                        .with(csrf())
                        .param("shortIntroduction", "not short".repeat(10))
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/account/config"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void configPrivate_changed() throws Exception {
        mockMvc.perform(
                post("/private")
                        .with(csrf())
                        .param("password", "newPassword!")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/@scene"))
        ;
        Account account = accountService.find("scene");
        boolean matches = passwordEncoder.matches("newPassword!", account.getPassword());
        assertThat(matches).isTrue();
    }

    @Test
    @WithAccount(username = "scene")
    void configPrivate_invalidParam_returnError() throws Exception {
        mockMvc.perform(
                post("/private")
                        .with(csrf())
                        .param("password", "short")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/account/config"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
        ;
    }

}
