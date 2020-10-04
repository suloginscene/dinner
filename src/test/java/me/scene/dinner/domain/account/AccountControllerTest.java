package me.scene.dinner.domain.account;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired SignupFormRepository tempRepository;
    @Autowired AccountRepository accountRepository;
    @MockBean JavaMailSender javaMailSender;

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
        then(javaMailSender).should().send(any(SimpleMailMessage.class));
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

    private void signupSubmit() throws Exception {
        mockMvc.perform(post(AccountController.URL_SIGNUP)
                .with(csrf())
                .param("username", "scene")
                .param("email", "scene@email.com")
                .param("password", "password")
                .param("agreement", "true"));
    }

    @Test
    void verifyEmail_store() throws Exception {
        signupSubmit();

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
        signupSubmit();

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
                .andExpect(view().name("page/error/dinner"))
        ;
        account = accountRepository.findByUsername(username).orElse(null);
        assertThat(account).isNull();
    }

}
