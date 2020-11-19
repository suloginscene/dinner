package me.scene.dinner.account.ui;

import me.scene.dinner.account.domain.account.TempPasswordIssuedEvent;
import me.scene.dinner.mail.MailSender;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.account.domain.tempaccount.TempAccount;
import me.scene.dinner.account.domain.tempaccount.TempAccountCreatedEvent;
import me.scene.dinner.account.domain.tempaccount.TempAccountRepository;
import me.scene.dinner.utils.authentication.WithAccount;
import me.scene.dinner.utils.factory.AccountFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

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
        then(mailSender).should().onApplicationEvent(any(TempAccountCreatedEvent.class));
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
                .andExpect(view().name("error/user_not_found"))
        ;
        account = accountRepository.findByUsername(username).orElse(null);
        assertThat(account).isNull();

        mockMvc.perform(
                get("/verify")
                        .param("email", email)
                        .param("token", "invalid-token")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/verification"))
        ;
        account = accountRepository.findByUsername(username).orElse(null);
        assertThat(account).isNull();
    }

    @Test
    void verify_alreadyVerified_handleException() throws Exception {
        TempAccount tempAccount = accountFactory.createTemp("scene", "scene@email.com", "password");

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

        mockMvc.perform(
                get("/verify")
                        .param("email", email)
                        .param("token", verificationToken)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/already_verified"))
        ;
    }

    @Test
    void loginPage_isCustomized() throws Exception {
        mockMvc.perform(
                get("/login")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/account/login"))
        ;
    }

    @Test
    void login_withUsername_authenticated() throws Exception {
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
    void login_withEmail_authenticated() throws Exception {
        accountFactory.create("scene", "scene@email.com", "password");

        mockMvc.perform(
                post("/login")
                        .with(csrf())
                        .param("username", "scene@email.com")
                        .param("password", "password")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated())
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void logout_unauthenticated() throws Exception {
        mockMvc.perform(get("/")).andExpect(authenticated());

        mockMvc.perform(
                post("/logout")
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(unauthenticated())
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
    void forgotPage_exists() throws Exception {
        mockMvc.perform(
                get("/forgot")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/account/forgot"))
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
        then(mailSender).should().onApplicationEvent(any(TempPasswordIssuedEvent.class));
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
