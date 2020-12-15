package me.scene.dinner.board.ui;

import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.board.application.common.BoardNotFoundException;
import me.scene.dinner.board.domain.magazine.Magazine;
import me.scene.dinner.board.domain.magazine.Policy;
import me.scene.dinner.board.domain.topic.Topic;
import me.scene.dinner.test.facade.FactoryFacade;
import me.scene.dinner.test.facade.RepositoryFacade;
import me.scene.dinner.test.proxy.service.MagazineServiceProxy;
import me.scene.dinner.test.proxy.service.TopicServiceProxy;
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

import static me.scene.dinner.test.utils.Authenticators.login;
import static me.scene.dinner.test.utils.Authenticators.logout;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Topic")
class TopicControllerTest {

    @Autowired MockMvc mockMvc;

    @SpyBean TopicServiceProxy topicService;
    @SpyBean MagazineServiceProxy magazineService;

    @Autowired FactoryFacade factoryFacade;
    @Autowired RepositoryFacade repositoryFacade;


    Account manager;
    Magazine magazine;
    Account user;

    @BeforeEach
    void setup() {
        manager = factoryFacade.createAccount("manager");
        magazine = factoryFacade.createMagazine(manager, "OPEN Magazine", Policy.OPEN);
        user = factoryFacade.createAccount("user");
        login(user);
    }

    @AfterEach
    void clear() {
        repositoryFacade.deleteAll();
    }


    @Nested
    class Create {

        @Nested
        class Page {

            @Test
            void returns_form() throws Exception {
                mockMvc.perform(
                        get("/topic-form")
                                .param("magazineId", magazine.getId().toString())
                )
                        .andExpect(status().isOk())
                        .andExpect(view().name("page/board/topic/form"))
                        .andExpect(model().attributeExists("topicForm"))
                ;
            }

            @Nested
            class When_Unauthenticated {
                @Test
                void redirectsTo_login() throws Exception {
                    logout();
                    mockMvc.perform(
                            get("/topic-form")
                    )
                            .andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrlPattern("**/login"))
                    ;
                }
            }

        }

        @Nested
        class Submit {
            @Nested
            class When_open {
                @Test
                void saves_And_redirectsTo_Topic() throws Exception {
                    mockMvc.perform(
                            post("/topics")
                                    .param("magazineId", magazine.getId().toString())
                                    .param("title", "Test Topic")
                                    .param("shortExplanation", "This is short explanation.")
                                    .param("longExplanation", "This is long explanation of test magazine.")
                                    .with(csrf())
                    )
                            .andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrlPattern("/topics/*"))
                    ;
                    Topic topic = topicService.load("Test Topic");
                    assertThat(topic.getShortExplanation()).isEqualTo("This is short explanation.");
                    assertThat(topic.getLongExplanation()).isEqualTo("This is long explanation of test magazine.");
                    assertThat(topic.getMagazine()).isEqualTo(magazine);
                    assertThat(topic.getOwner().getOwnerName()).isEqualTo(user.getUsername());
                }

                @Nested
                class With_invalid_params {
                    @Test
                    void returns_errors() throws Exception {
                        mockMvc.perform(
                                post("/topics")
                                        .with(csrf())
                                        .param("magazineId", magazine.getId().toString())
                        )
                                .andExpect(status().isOk())
                                .andExpect(view().name("page/board/topic/form"))
                                .andExpect(model().hasErrors())
                                .andExpect(model().errorCount(3))
                        ;
                    }
                }
            }

            @Nested
            class When_managed {
                Magazine managed;

                @BeforeEach
                void setup() {
                    managed = factoryFacade.createMagazine(manager, "MANAGED Magazine", Policy.MANAGED);
                }

                @Test
                void handles_exception() throws Exception {
                    mockMvc.perform(
                            post("/topics")
                                    .param("magazineId", managed.getId().toString())
                                    .param("title", "Test Topic")
                                    .param("shortExplanation", "This is short explanation.")
                                    .param("longExplanation", "This is long explanation of test magazine.")
                                    .with(csrf())
                    )
                            .andExpect(status().isOk())
                            .andExpect(view().name("error/access"));
                }

                @Nested
                class When_member {
                    @Test
                    void saves_and_redirectsTo_topic() throws Exception {
                        magazineService.addMember(managed, user);
                        mockMvc.perform(
                                post("/topics")
                                        .param("magazineId", managed.getId().toString())
                                        .param("title", "Test Topic")
                                        .param("shortExplanation", "This is short explanation.")
                                        .param("longExplanation", "This is long explanation of test magazine.")
                                        .with(csrf())
                        )
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrlPattern("/topics/*"))
                        ;
                    }
                }
            }

            @Nested
            class When_exclusive {
                Magazine exclusive;

                @BeforeEach
                void setup() {
                    exclusive = factoryFacade.createMagazine(manager, "EXCLUSIVE magazine", Policy.EXCLUSIVE);
                }

                @Test
                void handles_exception() throws Exception {
                    mockMvc.perform(
                            post("/topics")
                                    .param("magazineId", exclusive.getId().toString())
                                    .param("title", "Test Topic")
                                    .param("shortExplanation", "This is short explanation.")
                                    .param("longExplanation", "This is long explanation of test magazine.")
                                    .with(csrf())
                    )
                            .andExpect(status().isOk())
                            .andExpect(view().name("error/access"))
                    ;
                }

                @Nested
                class When_manager {
                    @Test
                    void saves_And_redirectTo_topic() throws Exception {
                        logout();
                        login(manager);
                        mockMvc.perform(
                                post("/topics")
                                        .param("magazineId", exclusive.getId().toString())
                                        .param("title", "Test Topic")
                                        .param("shortExplanation", "This is short explanation.")
                                        .param("longExplanation", "This is long explanation of test magazine.")
                                        .with(csrf())
                        )
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrlPattern("/topics/*"))
                        ;
                    }
                }

            }
        }

    }

    @Nested
    class Read {

        Topic topic;

        @BeforeEach
        void setup() {
            topic = factoryFacade.createTopic(magazine, manager, "Test Topic");
        }

        @Test
        void shows_topic() throws Exception {
            mockMvc.perform(
                    get("/topics/" + topic.getId())
            )
                    .andExpect(status().isOk())
                    .andExpect(view().name("page/board/topic/view"))
                    .andExpect(model().attributeExists("topic"))
            ;
        }

        @Nested
        class When_nonExistent {
            @Test
            void handles_exception() throws Exception {
                mockMvc.perform(
                        get("/topics/" + (topic.getId() + 1))
                )
                        .andExpect(status().isOk())
                        .andExpect(view().name("error/board_not_found"))
                ;
            }
        }

    }

    @Nested
    class Update {

        Topic topic;

        @BeforeEach
        void setup() {
            topic = factoryFacade.createTopic(magazine, user, "Test Topic");
        }

        @Nested
        class Page {
            @Test
            void returns_form() throws Exception {
                mockMvc.perform(
                        get("/topics/" + topic.getId() + "/form")
                )
                        .andExpect(status().isOk())
                        .andExpect(view().name("page/board/topic/update"))
                        .andExpect(model().attributeExists("updateForm"))
                ;
            }

            @Nested
            class With_notOwner {
                @Test
                void handles_exception() throws Exception {
                    logout();
                    login(manager);
                    mockMvc.perform(
                            get("/topics/" + topic.getId() + "/form")
                    )
                            .andExpect(status().isOk())
                            .andExpect(view().name("error/access"))
                    ;
                }
            }
        }

        @Nested
        class Submit {
            @Test
            void saves_And_redirectsTo_topic() throws Exception {
                Long id = topic.getId();
                mockMvc.perform(
                        put("/topics/" + id)
                                .with(csrf())
                                .param("id", id.toString())
                                .param("magazineId", magazine.getId().toString())
                                .param("title", "Updated")
                                .param("shortExplanation", "Updated short.")
                                .param("longExplanation", "Updated long.")
                )
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/topics/" + id))
                ;
                Topic topic = topicService.find(id);
                assertThat(topic.getTitle()).isEqualTo("Updated");
                assertThat(topic.getShortExplanation()).isEqualTo("Updated short.");
                assertThat(topic.getLongExplanation()).isEqualTo("Updated long.");
            }

            @Nested
            class With_invalid_params {
                @Test
                void redirectsTo_form() throws Exception {
                    Long id = topic.getId();
                    mockMvc.perform(
                            put("/topics/" + id)
                                    .with(csrf())
                                    .param("id", "")
                                    .param("magazineId", "")
                                    .param("title", "")
                                    .param("shortExplanation", "")
                                    .param("longExplanation", "")
                    )
                            .andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrl("/topics/" + id + "/form"))
                    ;
                }
            }

            @Nested
            class With_notOwner {
                @Test
                void handles_exception() throws Exception {
                    logout();
                    login(manager);
                    Long id = topic.getId();
                    mockMvc.perform(
                            put("/topics/" + id)
                                    .with(csrf())
                                    .param("id", id.toString())
                                    .param("magazineId", magazine.getId().toString())
                                    .param("title", "Updated")
                                    .param("shortExplanation", "Updated short.")
                                    .param("longExplanation", "Updated long.")
                    )
                            .andExpect(status().isOk())
                            .andExpect(view().name("error/access"))
                    ;
                }
            }
        }

    }

    @Nested
    class Delete {

        Topic topic;

        @BeforeEach
        void setup() {
            topic = factoryFacade.createTopic(magazine, user, "Test Topic");
        }

        @Test
        void deletes_And_redirectsTo_magazine() throws Exception {
            Long id = topic.getId();
            mockMvc.perform(
                    delete("/topics/" + id)
                            .with(csrf())
            )
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/magazines/" + magazine.getId()))
            ;
            assertThrows(BoardNotFoundException.class, () -> topicService.find(id));
        }

        @Nested
        class When_has_child {
            @Test
            void handles_Exception() throws Exception {
                factoryFacade.createArticle(topic, user, "Test Article", true);
                mockMvc.perform(
                        delete("/topics/" + topic.getId())
                                .with(csrf())
                )
                        .andExpect(status().isOk())
                        .andExpect(view().name("error/not_deletable"))
                ;
                assertDoesNotThrow(() -> topicService.find(topic.getId()));
            }
        }

        @Nested
        class With_notOwner {
            @Test
            void handles_exception() throws Exception {
                logout();
                login(manager);
                Long id = topic.getId();
                mockMvc.perform(
                        delete("/topics/" + id)
                                .with(csrf())
                )
                        .andExpect(status().isOk())
                        .andExpect(view().name("error/access"))
                ;
                assertDoesNotThrow(() -> topicService.find(id));
            }
        }

    }

}
