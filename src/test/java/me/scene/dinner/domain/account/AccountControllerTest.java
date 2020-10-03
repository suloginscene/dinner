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
    void verifyEmail_store() throws Exception {
        mockMvc.perform(post(AccountController.URL_SIGNUP)
                .with(csrf())
                .param("username", "scene")
                .param("email", "scene@email.com")
                .param("password", "password")
                .param("agreement", "true"));
        SignupForm signupForm = tempRepository.findByUsername("scene").orElseThrow();
        String verificationToken = signupForm.getVerificationToken();

        mockMvc.perform(
                get(AccountController.URL_VERIFY)
                        .param("email", "scene@email.com")
                        .param("token", verificationToken)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/account/verify"))
        ;

        Account account = accountRepository.findByUsername("scene").orElseThrow();
        assertThat(account).isNotNull();
    }

}
