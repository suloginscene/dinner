package me.scene.dinner.domain.account.ui;

import me.scene.dinner.domain.account.utils.AccountFactory;
import me.scene.dinner.domain.account.domain.Account;
import me.scene.dinner.domain.account.domain.AccountRepository;
import me.scene.dinner.domain.account.domain.TempAccount;
import me.scene.dinner.domain.account.domain.TempAccountRepository;
import me.scene.dinner.domain.account.application.MailSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class SignupControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired TempAccountRepository tempAccountRepository;
    @Autowired AccountRepository accountRepository;
    @Autowired AccountFactory accountFactory;
    @MockBean MailSender mailSender;

    @Test
    void getForm_hasForm() throws Exception {
        mockMvc.perform(
                get("/signup")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/account/signup"))
                .andExpect(model().attributeExists("accountForm"))
        ;
    }

    @Test
    void postForm_encodeStoreSend() throws Exception {
        mockMvc.perform(
                post("/signup")
                        .with(csrf())
                        .param("username", "scene")
                        .param("email", "scene@email.com")
                        .param("password", "password")
                        .param("agreement", "true")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sent?email=scene@email.com"))
        ;
        TempAccount tempAccount = tempAccountRepository.findByUsername("scene").orElseThrow();
        assertThat(tempAccount.getPassword()).isNotEqualTo("password");
        assertThat(tempAccount.getVerificationToken()).isNotNull();
        then(mailSender).should().send(anyString(), anyString(), anyString());
    }

    @Test
    void postForm_invalidParams_returnErrors() throws Exception {
        TempAccount tempAccount;

        mockMvc.perform(
                post("/signup")
                        .with(csrf())
                        .param("username", "[unacceptable]")
                        .param("email", "notAnEmail")
                        .param("password", "short")
                        .param("agreement", "false")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/account/signup"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(4))
        ;
        tempAccount = tempAccountRepository.findByUsername("[unacceptable]").orElse(null);
        assertThat(tempAccount).isNull();

        mockMvc.perform(
                post("/signup")
                        .with(csrf())
                        .param("username", "anonymousUser")
                        .param("email", "valid@email.com")
                        .param("password", "validPassword")
                        .param("agreement", "true")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/account/signup"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
        ;
        tempAccount = tempAccountRepository.findByUsername("anonymousUser").orElse(null);
        assertThat(tempAccount).isNull();
    }

    @Test
    void postForm_duplicated_returnErrors() throws Exception {
        accountFactory.create("scene", "scene@email.com", "password");

        mockMvc.perform(
                post("/signup")
                        .with(csrf())
                        .param("username", "scene")
                        .param("email", "scene@email.com")
                        .param("password", "password")
                        .param("agreement", "true")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/account/signup"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(2))
        ;
    }

    @Test
    void verify_store() throws Exception {
        TempAccount tempAccount = accountFactory.createTemp("scene", "scene@email.com", "password");

        String username = tempAccount.getUsername();
        String email = tempAccount.getEmail();
        String verificationToken = tempAccount.getVerificationToken();
        mockMvc.perform(
                get("/verify")
                        .param("email", email)
                        .param("token", verificationToken)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/account/welcome"))
        ;
        Account account = accountRepository.findByUsername(username).orElseThrow();
        assertThat(account).isNotNull();
    }

    @Test
    void verify_invalidParams_handleException() throws Exception {
        TempAccount tempAccount = accountFactory.createTemp("scene", "scene@email.com", "password");

        String username = tempAccount.getUsername();
        String email = tempAccount.getEmail();
        String verificationToken = tempAccount.getVerificationToken();
        Account account;

        mockMvc.perform(
                get("/verify")
                        .param("email", "invalid@email.com")
                        .param("token", verificationToken)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/error/user_not_found"))
        ;
        account = accountRepository.findByUsername(username).orElse(null);
        assertThat(account).isNull();

        mockMvc.perform(
                get("/verify")
                        .param("email", email)
                        .param("token", "invalid-token")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/error/verification"))
        ;
        account = accountRepository.findByUsername(username).orElse(null);
        assertThat(account).isNull();
    }

}
