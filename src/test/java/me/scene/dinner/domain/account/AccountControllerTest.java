package me.scene.dinner.domain.account;

import me.scene.dinner.MainController;
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
                get(AccountController.URL_SIGNUP)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/account/signup"))
                .andExpect(model().attributeExists("signupForm"))
        ;
    }

    @Test
    void signupSubmit_encodeStoreSend() throws Exception {
        mockMvc.perform(
                post(AccountController.URL_SIGNUP)
                        .with(csrf())
                        .param("username", "scene")
                        .param("email", "scene@email.com")
                        .param("password", "password")
                        .param("agreement", "true")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(AccountController.URL_VERIFY + "?*"))
        ;

        SignupForm signupForm = tempRepository.findByUsername("scene").orElseThrow();
        assertThat(signupForm.getPassword()).isNotEqualTo("password");
        assertThat(signupForm.getVerificationToken()).isNotNull();
        then(mailSender).should().send(any(MailMessage.class));
    }

    @Test
    void signupSubmit_invalidParams_returnErrors() throws Exception {
        mockMvc.perform(
                post(AccountController.URL_SIGNUP)
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

        SignupForm signupForm = tempRepository.findByUsername("[unacceptable]").orElse(null);
        assertThat(signupForm).isNull();
    }

    @Test
    void signupSubmit_duplicated_returnErrors() throws Exception {
        accountFactory.createInRegular("scene");

        mockMvc.perform(
                post(AccountController.URL_SIGNUP)
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
                get(AccountController.URL_VERIFY)
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
                get(AccountController.URL_VERIFY)
                        .param("email", "invalid@email.com")
                        .param("token", verificationToken)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/error/username_not_found"))
        ;
        account = accountRepository.findByUsername(username).orElse(null);
        assertThat(account).isNull();

        mockMvc.perform(
                get(AccountController.URL_VERIFY)
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
                get(AccountController.URL_LOGIN)
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
                .andExpect(redirectedUrlPattern("**" + AccountController.URL_LOGIN))
        ;
    }

    @Test
    void login_authenticated() throws Exception {
        accountFactory.createInRegular("scene");

        mockMvc.perform(
                post(AccountController.URL_LOGIN)
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
                post(AccountController.URL_LOGIN)
                        .with(csrf())
                        .param("username", "invalid")
                        .param("password", "password")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(AccountController.URL_LOGIN + "?error"))
                .andExpect(unauthenticated())
        ;

        mockMvc.perform(
                post(AccountController.URL_LOGIN)
                        .with(csrf())
                        .param("username", "scene")
                        .param("password", "invalid")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(AccountController.URL_LOGIN + "?error"))
                .andExpect(unauthenticated())
        ;
    }

    @Test
    void profilePage_nonExistent_handleException() throws Exception {
        mockMvc.perform(
                get(AccountController.URL_PROFILE + "/scene")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/error/username_not_found"))
        ;
    }

    @Test
    void profilePage_notOwner_readOnly() throws Exception {
        accountFactory.createInRegular("scene");

        mockMvc.perform(
                get(AccountController.URL_PROFILE + "/scene")
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
                get(AccountController.URL_PROFILE + "/scene")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/account/profile"))
                .andExpect(model().attribute("isOwner", true))
        ;
    }

}
