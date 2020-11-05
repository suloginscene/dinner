package me.scene.dinner.account.ui;

import me.scene.dinner.account.application.MailSender;
import me.scene.dinner.account.domain.Account;
import me.scene.dinner.account.utils.AccountFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountFactory accountFactory;
    @MockBean MailSender mailSender;

    @Test
    void page_isCustomized() throws Exception {
        mockMvc.perform(
                get("/login")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/account/login"))
        ;
    }

    @Test
    void login_authenticated() throws Exception {
        accountFactory.create("scene", "scene@email.com", "password");

        mockMvc.perform(
                post("/login")
                        .with(csrf())
                        .param("username", "scene")
                        .param("password", "password")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated())
        ;
    }

    @Test
    void login_invalidParams_unauthenticated() throws Exception {
        accountFactory.create("scene", "scene@email.com", "password");

        mockMvc.perform(
                post("/login")
                        .with(csrf())
                        .param("username", "invalid")
                        .param("password", "password")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated())
        ;

        mockMvc.perform(
                post("/login")
                        .with(csrf())
                        .param("username", "scene")
                        .param("password", "invalid")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated())
        ;
    }

    @Test
    void forgot_changeEncodeSend() throws Exception {
        Account scene = accountFactory.create("scene", "scene@email.com", "password");

        String encodedOldPassword = scene.getPassword();
        mockMvc.perform(
                post("/forgot")
                        .with(csrf())
                        .param("email", "scene@email.com")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sent?email=scene@email.com"))
        ;
        String encodedNewPassword = scene.getPassword();
        assertThat(encodedNewPassword).isNotEqualTo(encodedOldPassword);
        assertThat(encodedNewPassword).startsWith("{bcrypt}");
        then(mailSender).should().send(anyString(), anyString(), anyString());
    }

    @Test
    void forgot_invalidEmail_handleException() throws Exception {
        mockMvc.perform(
                post("/forgot")
                        .with(csrf())
                        .param("email", "non-existent@email.com")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/user_not_found"))
        ;
    }

}
