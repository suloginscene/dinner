package me.scene.dinner.domain.account;

import me.scene.dinner.domain.MainController;
import me.scene.dinner.domain.account.domain.Account;
import me.scene.dinner.domain.account.domain.AccountRepository;
import me.scene.dinner.domain.account.domain.SignupForm;
import me.scene.dinner.domain.account.domain.SignupFormRepository;
import me.scene.dinner.infra.mail.MailMessage;
import me.scene.dinner.infra.mail.MailSender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired SignupFormRepository tempRepository;
    @Autowired AccountRepository accountRepository;
    @Autowired AccountFactory accountFactory;
    @MockBean MailSender mailSender;

    @BeforeEach
    void beforeEach() {

    }

    @AfterEach
    void clearRepositories() {
        tempRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void signupPage_hasSignupForm() throws Exception {
        mockMvc.perform(
                get("/signup")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/account/signup"))
                .andExpect(model().attributeExists("signupForm"))
        ;
    }

    @Test
    void signupSubmit_encodeStoreSend() throws Exception {
        mockMvc.perform(
                post("/signup")
                        .with(csrf())
                        .param("username", "scene")
                        .param("email", "scene@email.com")
                        .param("password", "password")
                        .param("agreement", "true")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/verify?*"))
        ;

        SignupForm signupForm = tempRepository.findByUsername("scene").orElseThrow();
        assertThat(signupForm.getPassword()).isNotEqualTo("password");
        assertThat(signupForm.getVerificationToken()).isNotNull();
        then(mailSender).should().send(any(MailMessage.class));
    }

    @Test
    void signupSubmit_invalidParams_returnErrors() throws Exception {
        SignupForm signupForm;

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

        signupForm = tempRepository.findByUsername("[unacceptable]").orElse(null);
        assertThat(signupForm).isNull();

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

        signupForm = tempRepository.findByUsername("anonymousUser").orElse(null);
        assertThat(signupForm).isNull();
    }

    @Test
    void signupSubmit_duplicated_returnErrors() throws Exception {
        accountFactory.createInRegular("scene");

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
    void verifyEmail_store() throws Exception {
        accountFactory.createInTemp("scene");

        SignupForm signupForm = tempRepository.findByUsername("scene").orElseThrow();
        String username = signupForm.getUsername();
        String email = signupForm.getEmail();
        String verificationToken = signupForm.getVerificationToken();

        mockMvc.perform(
                get("/verify")
                        .param("email", email)
                        .param("token", verificationToken)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/account/verify"))
        ;

        Account account = accountRepository.findByUsername(username).orElseThrow();
        assertThat(account).isNotNull();
    }

    @Test
    void verifyEmail_invalidParams_handleException() throws Exception {
        accountFactory.createInTemp("scene");

        SignupForm signupForm = tempRepository.findByUsername("scene").orElseThrow();
        String username = signupForm.getUsername();
        String email = signupForm.getEmail();
        String verificationToken = signupForm.getVerificationToken();

        Account account;

        mockMvc.perform(
                get("/verify")
                        .param("email", "invalid@email.com")
                        .param("token", verificationToken)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/error/username_not_found"))
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

    @Test
    void loginPage_isCustomizedPage() throws Exception {
        mockMvc.perform(
                get("/login")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/account/login"))
        ;
    }

    @Test
    void loginPage_approachForbiddenPage_beGuidedBySpringSecurity() throws Exception {
        mockMvc.perform(
                get("/unauthenticated-user-is-forbidden-to-approach-this-page")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"))
        ;
    }

    @Test
    void login_authenticated() throws Exception {
        accountFactory.createInRegular("scene");

        mockMvc.perform(
                post("/login")
                        .with(csrf())
                        .param("username", "scene")
                        .param("password", "password")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(MainController.URL_HOME))
                .andExpect(authenticated())
        ;
    }

    @Test
    void login_invalidParams_unauthenticated() throws Exception {
        accountFactory.createInRegular("scene");

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
    @Transactional
    void forgotPassword_changePassword() throws Exception {
        accountFactory.createInRegular("scene");
        Account scene = accountRepository.findByUsername("scene").orElseThrow();
        String encodedOldPassword = scene.getPassword();

        mockMvc.perform(
                post("/forgot")
                        .with(csrf())
                        .param("email", "scene@email.com")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/forgot?*"))
        ;
        then(mailSender).should().send(any(MailMessage.class));

        String encodedNewPassword = scene.getPassword();
        assertThat(encodedNewPassword).startsWith("{bcrypt}");
        assertThat(encodedNewPassword).isNotEqualTo(encodedOldPassword);
    }

    @Test
    void forgotPassword_invalidEmail_handleException() throws Exception {
        mockMvc.perform(
                post("/forgot")
                        .with(csrf())
                        .param("email", "non-existent@email.com")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/error/username_not_found"))
        ;
    }

    @Test
    void profilePage_nonExistent_handleException() throws Exception {
        mockMvc.perform(
                get("/accounts/scene")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/error/username_not_found"))
        ;
    }

    @Test
    void profilePage_notOwner_readOnly() throws Exception {
        accountFactory.createInRegular("scene");

        mockMvc.perform(
                get("/accounts/scene")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/account/profile"))
                .andExpect(model().attribute("isOwner", false))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void profilePage_owner_modifiable() throws Exception {
        mockMvc.perform(
                get("/accounts/scene")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/account/profile"))
                .andExpect(model().attribute("isOwner", true))
        ;
    }

    // TODO temporary version
    @Test
    @Transactional
    @WithAccount(username = "scene")
    void changePassword_changed() throws Exception {
        Account scene = accountRepository.findByUsername("scene").orElseThrow();
        String encodedOldPassword = scene.getPassword();

        mockMvc.perform(
                post("/accounts/scene")
                        .with(csrf())
                        .param("password", "newPassword")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accounts/scene?success"))
        ;

        String encodedNewPassword = scene.getPassword();
        assertThat(encodedNewPassword).startsWith("{bcrypt}");
        assertThat(encodedNewPassword).isNotEqualTo(encodedOldPassword);
    }

    // TODO temporary version
    @Test
    @Transactional
    @WithAccount(username = "scene")
    void changePassword_short_unchanged() throws Exception {
        Account scene = accountRepository.findByUsername("scene").orElseThrow();
        String encodedOldPassword = scene.getPassword();

        mockMvc.perform(
                post("/accounts/scene")
                        .with(csrf())
                        .param("password", "short")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accounts/scene?short"))
        ;

        String encodedNewPassword = scene.getPassword();
        assertThat(encodedNewPassword).isEqualTo(encodedOldPassword);
    }

    // TODO temporary version
    @Test
    @Transactional
    void changePassword_notOwner_handleException() throws Exception {
        accountFactory.createInRegular("scene");

        Account scene = accountRepository.findByUsername("scene").orElseThrow();
        String encodedOldPassword = scene.getPassword();

        mockMvc.perform(
                post("/accounts/scene")
                        .with(csrf())
                        .param("password", "newPassword")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/error/forbidden"))
        ;

        String encodedNewPassword = scene.getPassword();
        assertThat(encodedNewPassword).isEqualTo(encodedOldPassword);
    }

}
