package me.scene.dinner.account.ui;

import me.scene.dinner.account.application.AccountService;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.account.Profile;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static me.scene.dinner.test.utils.authentication.Authenticators.login;
import static me.scene.dinner.test.utils.authentication.Authenticators.logout;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Config")
class ConfigControllerTest {

    @Autowired MockMvc mockMvc;

    @Autowired AccountService accountService;
    @Autowired PasswordEncoder passwordEncoder;

    @Autowired FactoryFacade factoryFacade;
    @Autowired RepositoryFacade repositoryFacade;


    Account user;

    @BeforeEach
    void setup() {
        user = factoryFacade.createAccount("user");
        accountService.updateProfile(user.getUsername(), "Test Introduction.");
        login(user);
    }

    @AfterEach
    void clear() {
        repositoryFacade.deleteAll();
    }


    @Nested
    class Info {

        @Test
        void returns_info() throws Exception {
            mockMvc.perform(
                    get("/@user")
            )
                    .andExpect(status().isOk())
                    .andExpect(view().name("page/account/info"))
                    .andExpect(model().attributeExists("username", "profile"))
            ;
        }

        @Nested
        class When_nonExistent {
            @Test
            void handles_exception() throws Exception {
                mockMvc.perform(
                        get("/@nonExistent")
                )
                        .andExpect(status().isOk())
                        .andExpect(view().name("error/user_not_found"))
                ;
            }
        }

    }

    @Nested
    class Profiles {

        @Nested
        class Page {
            @Test
            void returns_form() throws Exception {
                mockMvc.perform(
                        get("/profile")
                )
                        .andExpect(status().isOk())
                        .andExpect(view().name("page/account/profile"))
                        .andExpect(model().attributeExists("profileForm"))
                ;
            }

            @Nested
            class When_unauthenticated {
                @Test
                void redirectsTo_login() throws Exception {
                    logout();
                    mockMvc.perform(
                            get("/profile")
                    )
                            .andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrlPattern("**/login"))
                    ;
                }
            }
        }

        @Nested
        class Submit {
            @Test
            void saves_And_redirectsTo_info() throws Exception {
                mockMvc.perform(
                        post("/profile")
                                .with(csrf())
                                .param("username", user.getUsername())
                                .param("email", user.getEmail())
                                .param("introduction", "Hello")
                )
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/@user"))
                ;
                user = accountService.find(user.getUsername());
                assertThat(user.getProfile()).isEqualTo(new Profile("Hello"));
            }

            @Nested
            class With_invalid_params {
                @Test
                void returns_errors() throws Exception {
                    mockMvc.perform(
                            post("/profile")
                                    .with(csrf())
                                    .param("username", "valid")
                                    .param("email", "valid@email.com")
                                    .param("introduction", "Invalid Introduction.".repeat(10))
                    )
                            .andExpect(status().isOk())
                            .andExpect(view().name("page/account/profile"))
                            .andExpect(model().hasErrors())
                            .andExpect(model().errorCount(1))
                    ;
                }
            }
        }

    }

    @Nested
    class Password {

        @Nested
        class Page {
            @Test
            void returns_form() throws Exception {
                mockMvc.perform(
                        get("/password")
                )
                        .andExpect(status().isOk())
                        .andExpect(view().name("page/account/password"))
                        .andExpect(model().attributeExists("passwordForm"))
                ;
            }

            @Nested
            class When_unauthenticated {
                @Test
                void redirectsTo_login() throws Exception {
                    logout();
                    mockMvc.perform(
                            get("/password")
                    )
                            .andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrlPattern("**/login"))
                    ;
                }
            }
        }

        @Nested
        class Submit {
            @Test
            void saves_And_redirectsTo_info() throws Exception {
                mockMvc.perform(
                        post("/password")
                                .with(csrf())
                                .param("password", "newPassword!")
                )
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/@user"))
                ;
                user = accountService.find(user.getUsername());
                assertThat(passwordEncoder).matches(p -> p.matches("newPassword!", user.getPassword()));
            }

            @Nested
            class With_invalid_params {
                @Test
                void returns_errors() throws Exception {
                    mockMvc.perform(
                            post("/password")
                                    .with(csrf())
                                    .param("password", "short")
                    )
                            .andExpect(status().isOk())
                            .andExpect(view().name("page/account/password"))
                            .andExpect(model().hasErrors())
                            .andExpect(model().errorCount(1))
                    ;
                }
            }
        }

    }

}
