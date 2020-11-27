package me.scene.dinner.mail.ui;

import me.scene.dinner.account.application.AccountService;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.tempaccount.TempAccount;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.magazine.domain.Policy;
import me.scene.dinner.mail.infra.TestMailSender;
import me.scene.dinner.mail.service.AsyncMessagingException;
import me.scene.dinner.mail.service.SyncMessagingException;
import me.scene.dinner.test.facade.FactoryFacade;
import me.scene.dinner.test.facade.RepositoryFacade;
import me.scene.dinner.test.proxy.MagazineServiceProxy;
import me.scene.dinner.test.utils.authentication.Authenticators;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Mail")
class MailControllerTest {

    @Autowired MockMvc mockMvc;
    @SpyBean TestMailSender mailSender;

    @Autowired AccountService accountService;
    @SpyBean MagazineServiceProxy magazineService;

    @Autowired FactoryFacade factoryFacade;
    @Autowired RepositoryFacade repositoryFacade;


    Thread testThread;
    Thread mailThread;

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

        @Test
        void shows_magazine() throws Exception {
            Account user = factoryFacade.createAccount("user");
            Authenticators.login(user);
            mockMvc.perform(
                    get("/sent-to-manager")
                            .param("magazineId", "1")
            )
                    .andExpect(status().isOk())
                    .andExpect(view().name("page/mail/manager"))
            ;
        }

    }

    @Nested
    class When_exception {

        @Nested
        class Sync {

            @BeforeEach
            void setup() {
                doThrow(SyncMessagingException.class).when(mailSender).sync();
                testThread = Thread.currentThread();
            }

            @AfterEach
            void check() {
                mailThread = mailSender.lastThread();
                assertThat(mailThread).isEqualTo(testThread);
                then(mailSender).should(atLeastOnce()).sync();
            }

            @Nested
            class OnTempAccountCreated {
                @Test
                void rollBack() throws Exception {
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
                    Account user = Account.create(TempAccount.create("user", "user@email.com", "encoded"));
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
                    user = accountService.find(user.getUsername());
                    String newEncodedPassword = user.getPassword();
                    assertThat(newEncodedPassword).isEqualTo(oldEncodedPassword);
                }
            }

            @Nested
            class OnMemberApplied {
                @Test
                void rollBack_And_handles_Exception() throws Exception {
                    Account manager = Account.create(TempAccount.create("manager", "manager@email.com", "encoded"));
                    repositoryFacade.save(manager);
                    Account member = Account.create(TempAccount.create("member", "member@email.com", "encoded"));
                    repositoryFacade.save(member);
                    Magazine managed = factoryFacade.createMagazine(manager, "Test Magazine", Policy.MANAGED);
                    Authenticators.login(member);
                    mockMvc.perform(
                            post("/magazines/" + managed.getId() + "/members")
                                    .with(csrf())
                    )
                            .andExpect(status().isOk())
                            .andExpect(view().name("error/messaging"))
                    ;
                }
            }

        }

        @Nested
        class Async {

            @BeforeEach
            void setup() {
                doThrow(AsyncMessagingException.class).when(mailSender).async();
                testThread = Thread.currentThread();
            }

            @AfterEach
            void check() {
                mailThread = mailSender.lastThread();
                assertThat(mailThread).isNotNull();
                assertThat(mailThread).isNotEqualTo(testThread);
                then(mailSender).should(atLeastOnce()).async();
            }

            @Nested
            class OnMemberQuit {
                @Test
                void notRollBack() throws Exception {
                    Account manager = factoryFacade.createAccount("manager");
                    Magazine managed = factoryFacade.createMagazine(manager, "Test Magazine", Policy.MANAGED);
                    Account member = factoryFacade.createAccount("member");
                    magazineService.addMember(managed, member);
                    Authenticators.login(member);
                    mockMvc.perform(
                            delete("/magazines/" + managed.getId() + "/members")
                                    .with(csrf())
                    )
                            .andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrl("/magazines/" + managed.getId()))
                    ;
                    Thread.sleep(1000L);
                    List<String> members = magazineService.getMembers(managed.getId());
                    assertThat(members).doesNotContain(member.getUsername());
                }
            }

            @Nested
            class OnMemberManaged {
                @Test
                void notRollBack() throws Exception {
                    Account manager = factoryFacade.createAccount("manager");
                    Magazine managed = factoryFacade.createMagazine(manager, "Test Magazine", Policy.MANAGED);
                    Account member = factoryFacade.createAccount("member");
                    Authenticators.login(manager);
                    mockMvc.perform(
                            post("/magazines/" + managed.getId() + "/" + member.getUsername())
                                    .param("memberEmail", member.getEmail())
                                    .with(csrf())
                    )
                            .andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrl("/magazines/" + managed.getId() + "/members"))
                    ;
                    Thread.sleep(1000L);
                    List<String> members = magazineService.getMembers(managed.getId());
                    assertThat(members).contains(member.getUsername());
                }
            }
        }

    }

}
