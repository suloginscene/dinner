package me.scene.dinner.account.ui;

import me.scene.dinner.account.application.query.dto.AccountDto;
import me.scene.dinner.account.application.query.AccountQueryService;
import me.scene.dinner.account.application.command.AccountService;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.account.application.command.event.TempPasswordIssuedEvent;
import me.scene.dinner.account.domain.tempaccount.TempAccount;
import me.scene.dinner.account.application.command.event.TempAccountCreatedEvent;
import me.scene.dinner.mail.application.MailListener;
import me.scene.dinner.test.facade.FactoryFacade;
import me.scene.dinner.test.facade.RepositoryFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import static me.scene.dinner.test.utils.Authenticators.login;
import static me.scene.dinner.test.utils.Authenticators.logout;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Account")
class AccountControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean MailListener mailListener;

    @Autowired AccountService accountService;
    @Autowired AccountQueryService accountQueryService;
    @Autowired AccountRepository accountRepository;

    @Autowired FactoryFacade factoryFacade;
    @Autowired RepositoryFacade repositoryFacade;


    @AfterEach
    void clear() {
        repositoryFacade.deleteAll();
    }


    @Nested
    class Signup {

        @Nested
        class Page {
            @Test
            void returns_form() throws Exception {
                mockMvc.perform(
                        get("/signup")
                )
                        .andExpect(status().isOk())
                        .andExpect(view().name("page/account/signup"))
                        .andExpect(model().attributeExists("signupForm"))
                ;
            }
        }

        @Nested
        class Submit {
            @Test
            void encodes_saves_sends_And_redirectsTo_sent() throws Exception {
                mockMvc.perform(
                        post("/signup")
                                .with(csrf())
                                .param("username", "scene")
                                .param("email", "scene@email.com")
                                .param("password", "password")
                                .param("agreement", "true")
                )
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/sent-to-account?email=scene@email.com"))
                ;
                TempAccount tempAccount = repositoryFacade.findTempByUsername("scene").orElseThrow();
                assertThat(tempAccount.getPassword()).isNotEqualTo("password");
                assertThat(tempAccount.getVerificationToken()).isNotNull();
                TempAccountCreatedEvent event = new TempAccountCreatedEvent(tempAccount.getEmail(), tempAccount.getVerificationToken());
                then(mailListener).should().onApplicationEvent(event);
            }

            @Nested
            class With_invalid_params {
                @Test
                void returns_errors() throws Exception {
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
                            .andExpect(model().errorCount(4))
                    ;
                    TempAccount temp1 = repositoryFacade.findTempByUsername("[unacceptable]").orElse(null);
                    assertThat(temp1).isNull();

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
                            .andExpect(model().errorCount(2))
                            .andExpect(model().attributeHasFieldErrors("signupForm", "username"))
                    ;
                    TempAccount temp2 = repositoryFacade.findTempByUsername("anonymousUser").orElse(null);
                    assertThat(temp2).isNull();
                }
            }

            @Nested
            class When_duplicated_user_exists {
                @Test
                void returns_errors() throws Exception {
                    Account user = factoryFacade.createAccount("user");

                    mockMvc.perform(
                            post("/signup")
                                    .with(csrf())
                                    .param("username", user.getUsername())
                                    .param("email", user.getEmail())
                                    .param("password", "password")
                                    .param("agreement", "true")
                    )
                            .andExpect(status().isOk())
                            .andExpect(view().name("page/account/signup"))
                            .andExpect(model().hasErrors())
                            .andExpect(model().errorCount(2))
                    ;
                }
            }
        }

    }

    @Nested
    class Verify {

        TempAccount tempUser;

        @BeforeEach
        void setup() {
            tempUser = factoryFacade.createTempAccount("user");
        }

        @Test
        void stores_And_show_welcome() throws Exception {
            String username = tempUser.getUsername();
            String email = tempUser.getEmail();
            String verificationToken = tempUser.getVerificationToken();
            mockMvc.perform(
                    get("/verify")
                            .param("email", email)
                            .param("token", verificationToken)
            )
                    .andExpect(status().isOk())
                    .andExpect(view().name("page/account/welcome"))
            ;
            AccountDto user = accountQueryService.findDto(username);
            assertThat(user.getEmail()).isEqualTo(email);
        }

        @Nested
        class With_invalid_params {
            @Test
            void handles_exception() throws Exception {
                String username = tempUser.getUsername();
                String email = tempUser.getEmail();
                String verificationToken = tempUser.getVerificationToken();

                mockMvc.perform(
                        get("/verify")
                                .param("email", "invalid@email.com")
                                .param("token", verificationToken)
                )
                        .andExpect(status().isOk())
                        .andExpect(view().name("error/user_not_found"))
                ;
                assertThrows(UsernameNotFoundException.class, () -> accountQueryService.findDto(username));

                mockMvc.perform(
                        get("/verify")
                                .param("email", email)
                                .param("token", "invalid-token")
                )
                        .andExpect(status().isOk())
                        .andExpect(view().name("error/verification"))
                ;
                assertThrows(UsernameNotFoundException.class, () -> accountQueryService.findDto(username));
            }
        }

        @Nested
        class When_already_verified {
            @Test
            void handles_exception() throws Exception {
                String email = tempUser.getEmail();
                String verificationToken = tempUser.getVerificationToken();
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
        }

    }

    @Nested
    class Login {

        Account user;

        @BeforeEach
        void setup() {
            user = factoryFacade.createAccount("user");
        }

        @Nested
        class Page {
            @Test
            void show_customized() throws Exception {
                mockMvc.perform(
                        get("/login")
                )
                        .andExpect(status().isOk())
                        .andExpect(view().name("page/account/login"))
                ;
            }
        }

        @Nested
        class Submit {
            @Test
            void authenticate() throws Exception {
                mockMvc.perform(
                        post("/login")
                                .with(csrf())
                                .param("username", user.getUsername())
                                .param("password", "password")
                )
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/"))
                        .andExpect(authenticated())
                ;
                mockMvc.perform(get("/")).andExpect(unauthenticated());
                mockMvc.perform(
                        post("/login")
                                .with(csrf())
                                .param("username", user.getEmail())
                                .param("password", "password")
                )
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/"))
                        .andExpect(authenticated())
                ;
            }

            @Nested
            class With_invalid_param {
                @Test
                void not_authenticate_And_return_error() throws Exception {
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
                                    .param("username", user.getUsername())
                                    .param("password", "invalid")
                    )
                            .andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrl("/login?error"))
                            .andExpect(unauthenticated())
                    ;
                }
            }
        }

    }

    @Nested
    class Logout {

        @BeforeEach
        void setup() {
            Account user = factoryFacade.createAccount("user");
            login(user);
        }

        @Test
        void unauthenticate() throws Exception {
            mockMvc.perform(
                    post("/logout")
                            .with(csrf())
            )
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/"))
                    .andExpect(unauthenticated())
            ;
        }

    }

    @Nested
    class Forgot {

        Account user;

        @BeforeEach
        void setup() {
            user = factoryFacade.createAccount("user");
        }

        @Nested
        class Page {
            @Test
            void show_forgot() throws Exception {
                mockMvc.perform(
                        get("/forgot")
                )
                        .andExpect(status().isOk())
                        .andExpect(view().name("page/account/forgot"))
                ;
            }
        }

        @Nested
        class Submit {
            @Test
            void changes_encodes_sends_And_redirectsTo_sent() throws Exception {
                String oldEncodedPassword = user.getPassword();
                mockMvc.perform(
                        post("/forgot")
                                .with(csrf())
                                .param("email", user.getEmail())
                )
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/sent-to-account?email=" + user.getEmail()))
                ;
                user = accountRepository.findByUsername(user.getUsername()).orElseThrow();
                String newEncodedPassword = user.getPassword();
                assertThat(newEncodedPassword).isNotEqualTo(oldEncodedPassword);
                assertThat(newEncodedPassword).startsWith("{bcrypt}");
                TempPasswordIssuedEvent event = new TempPasswordIssuedEvent(user.getEmail(), "rawPassword");
                then(mailListener).should().onApplicationEvent(event);
            }

            @Nested
            class With_nonExistent_Email {
                @Test
                void handles_exception() throws Exception {
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
        }

    }

    @Nested
    class Api_Find {

        Account target;

        @BeforeEach
        void setup() {
            Account user = factoryFacade.createAccount("user");
            login(user);
            target = factoryFacade.createAccount("target");
        }

        @Test
        void returns_json() throws Exception {
            mockMvc.perform(
                    get("/api/accounts/" + target.getUsername())
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.*", hasSize(2)))
                    .andExpect(jsonPath("$.username").exists())
                    .andExpect(jsonPath("$.email").exists())
                    .andExpect(jsonPath("$.password").doesNotExist())
            ;
        }

        @Nested
        class With_nonExistent_username {
            @Test
            void returns_notFound() throws Exception {
                mockMvc.perform(
                        get("/api/accounts/" + "nonExistent")
                )
                        .andExpect(status().isNotFound())
                ;
            }
        }

        @Nested
        class When_unauthenticated {
            @BeforeEach
            void setup() {
                logout();
            }

            @Test
            void redirectsTo_login() throws Exception {
                mockMvc.perform(
                        get("/api/accounts/" + target.getUsername())
                )
                        .andExpect(status().is3xxRedirection())
                ;
            }
        }
    }

}
