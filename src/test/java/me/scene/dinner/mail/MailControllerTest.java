package me.scene.dinner.mail;

import me.scene.dinner.account.application.command.AccountService;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.application.command.event.TempPasswordIssuedEvent;
import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.account.domain.tempaccount.TempAccount;
import me.scene.dinner.account.application.command.event.TempAccountCreatedEvent;
import me.scene.dinner.test.facade.FactoryFacade;
import me.scene.dinner.test.facade.RepositoryFacade;
import me.scene.dinner.test.proxy.service.MagazineServiceProxy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Mail")
class MailControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean MailSender mailSender;

    @Autowired AccountService accountService;
    @Autowired AccountRepository accountRepository;
    @SpyBean MagazineServiceProxy magazineService;

    @Autowired FactoryFacade factoryFacade;
    @Autowired RepositoryFacade repositoryFacade;


    @AfterEach
    void clear() {
        repositoryFacade.deleteAll();
        reset(mailSender);
    }


    @Nested
    class Sent {

        @Test
        void shows_email() throws Exception {
            mockMvc.perform(
                    get("/sent-to-account?email=test@email.com")
            )
                    .andExpect(status().isOk())
                    .andExpect(view().name("page/mail/account"))
                    .andExpect(model().attributeExists("email"))
            ;
        }

    }

    @Nested
    class When_exception {

        @Nested
        class OnTempAccountCreated {
            @Test
            void rollBack() throws Exception {
                doThrow(RuntimeMessagingException.class).when(mailSender).onApplicationEvent(any(TempAccountCreatedEvent.class));
                mockMvc.perform(
                        post("/signup")
                                .with(csrf())
                                .param("username", "scene")
                                .param("email", "scene@email.com")
                                .param("password", "password")
                                .param("agreement", "true")
                )
                        .andExpect(status().isOk())
                        .andExpect(view().name("error/messaging"))
                ;
                TempAccount account = repositoryFacade.findTempByUsername("scene").orElse(null);
                assertThat(account).isNull();
            }
        }

        @Nested
        class OnTempPasswordIssued {
            @Test
            void rollBack_And_handles_Exception() throws Exception {
                doThrow(RuntimeMessagingException.class).when(mailSender).onApplicationEvent(any(TempPasswordIssuedEvent.class));
                Account user = new Account("user", "user@email.com", "encoded");
                repositoryFacade.save(user);
                String oldEncodedPassword = user.getPassword();
                mockMvc.perform(
                        post("/forgot")
                                .with(csrf())
                                .param("email", user.getEmail())
                )
                        .andExpect(status().isOk())
                        .andExpect(view().name("error/messaging"))
                ;
                user = accountRepository.findByUsername(user.getUsername()).orElseThrow();
                String newEncodedPassword = user.getPassword();
                assertThat(newEncodedPassword).isEqualTo(oldEncodedPassword);
            }
        }

    }

}
