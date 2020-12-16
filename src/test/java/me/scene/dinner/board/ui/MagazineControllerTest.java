package me.scene.dinner.board.ui;

import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.board.magazine.application.MagazineBestListCache;
import me.scene.dinner.board.magazine.application.MagazineNotFoundException;
import me.scene.dinner.board.magazine.application.MagazineSimpleDto;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.magazine.domain.Policy;
import me.scene.dinner.board.magazine.domain.event.MemberAddedEvent;
import me.scene.dinner.board.magazine.domain.event.MemberAppliedEvent;
import me.scene.dinner.board.magazine.domain.event.MemberQuitEvent;
import me.scene.dinner.board.magazine.domain.event.MemberRemovedEvent;
import me.scene.dinner.notification.NotificationListener;
import me.scene.dinner.test.facade.FactoryFacade;
import me.scene.dinner.test.facade.RepositoryFacade;
import me.scene.dinner.test.proxy.service.MagazineServiceProxy;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static me.scene.dinner.test.utils.Authenticators.login;
import static me.scene.dinner.test.utils.Authenticators.logout;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Magazine")
class MagazineControllerTest {

    @Autowired MockMvc mockMvc;
    @SpyBean NotificationListener notificationListener;

    @SpyBean MagazineServiceProxy magazineService;
    @SpyBean MagazineBestListCache bestListCache;

    @Autowired FactoryFacade factoryFacade;
    @Autowired RepositoryFacade repositoryFacade;


    Account manager;

    @BeforeEach
    void setUp() {
        manager = factoryFacade.createAccount("manager");
        login(manager);
    }

    void expectRedirectionToLogin(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @AfterEach
    void clear() {
        repositoryFacade.deleteAll();
        reset(notificationListener);
    }


    @Nested
    class Create {

        @Nested
        class Page {
            @Test
            void returns_form() throws Exception {
                mockMvc.perform(
                        get("/magazine-form")
                )
                        .andExpect(status().isOk())
                        .andExpect(view().name("page/board/magazine/form"))
                        .andExpect(model().attributeExists("magazineForm"))
                ;
            }

            @Nested
            class When_Unauthenticated {
                @Test
                void redirectsTo_login() throws Exception {
                    logout();
                    expectRedirectionToLogin(get("/magazine-form"));
                }
            }
        }

        @Nested
        class Submit {
            @Test
            void saves_updatesList_And_show_magazine() throws Exception {
                String title = "Test Magazine";
                String shortExplanation = "This is short explanation.";
                String longExplanation = "This is long explanation of test magazine.";
                mockMvc.perform(
                        post("/magazines")
                                .with(csrf())
                                .param("title", title)
                                .param("shortExplanation", shortExplanation)
                                .param("longExplanation", longExplanation)
                                .param("policy", "OPEN")
                )
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrlPattern("/magazines/*"))
                ;
                Magazine magazine = magazineService.load(title);
                assertThat(magazine.getShortExplanation()).isEqualTo(shortExplanation);
                assertThat(magazine.getLongExplanation()).isEqualTo(longExplanation);
                assertThat(magazine.getPolicy()).isEqualTo(Policy.OPEN);
                assertThat(magazine.getOwner().getOwnerName()).isEqualTo(manager.getUsername());
                List<MagazineSimpleDto> magazineDtos = bestListCache.getBestMagazines();
                assertThat(magazineDtos.stream().anyMatch(m -> m.getId().equals(magazine.getId()))).isTrue();
            }

            @Nested
            class With_invalid_params {
                @Test
                void returns_errors() throws Exception {
                    mockMvc.perform(
                            post("/magazines")
                                    .with(csrf())
                    )
                            .andExpect(status().isOk())
                            .andExpect(view().name("page/board/magazine/form"))
                            .andExpect(model().hasErrors())
                            .andExpect(model().errorCount(4))
                    ;
                }
            }

            @Nested
            class When_Unauthenticated {
                @Test
                void redirectsTo_login() throws Exception {
                    logout();
                    expectRedirectionToLogin(post("/magazines").with(csrf()));
                }
            }
        }

    }

    @Nested
    class Read {

        Magazine magazine;

        @BeforeEach
        void setup() {
            magazine = factoryFacade.createMagazine(manager, "Test Magazine", Policy.OPEN);
            logout();
        }

        @Test
        void show_magazine() throws Exception {
            mockMvc.perform(
                    get("/magazines/" + magazine.getId())
            )
                    .andExpect(status().isOk())
                    .andExpect(view().name("page/board/magazine/view"))
                    .andExpect(model().attributeExists("magazine"))
            ;
        }

        @Nested
        class With_nonExistent_magazine {
            @Test
            void handles_exception() throws Exception {
                mockMvc.perform(
                        get("/magazines/" + (magazine.getId() + 1))
                )
                        .andExpect(status().isOk())
                        .andExpect(view().name("error/board_not_found"))
                ;
            }
        }

    }

    @Nested
    class Update {

        Magazine magazine;

        @BeforeEach
        void setup() {
            magazine = factoryFacade.createMagazine(manager, "Test Magazine", Policy.OPEN);
        }

        @Nested
        class Page {
            @Test
            void returns_form() throws Exception {
                mockMvc.perform(
                        get("/magazines/" + magazine.getId() + "/form")
                )
                        .andExpect(status().isOk())
                        .andExpect(view().name("page/board/magazine/update"))
                        .andExpect(model().attributeExists("updateForm"))
                ;
            }

            @Nested
            class With_stranger {
                @Test
                void handles_exception() throws Exception {
                    logout();
                    Account stranger = factoryFacade.createAccount("stranger");
                    login(stranger);

                    mockMvc.perform(
                            get("/magazines/" + magazine.getId() + "/form")
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
            void updates_list_And_redirectsTo_magazine() throws Exception {
                Long id = magazine.getId();
                mockMvc.perform(
                        put("/magazines/" + id)
                                .with(csrf())
                                .param("id", id.toString())
                                .param("title", "Updated")
                                .param("shortExplanation", "Updated short.")
                                .param("longExplanation", "Updated long.")
                                .param("policy", "OPEN")
                )
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/magazines/" + id))
                ;
                Magazine magazine = magazineService.find(id);
                assertThat(magazine.getTitle()).isEqualTo("Updated");
                assertThat(magazine.getShortExplanation()).isEqualTo("Updated short.");
                assertThat(magazine.getLongExplanation()).isEqualTo("Updated long.");
                List<MagazineSimpleDto> magazineDtos = bestListCache.getBestMagazines();
                assertThat(magazineDtos.stream().anyMatch(m -> m.getId().equals(id))).isTrue();
                assertThat(magazineDtos.stream().filter(m -> m.getId().equals(id)).findFirst().orElseThrow().getTitle()).isEqualTo("Updated");
            }

            @Nested
            class With_invalid_params {
                @Test
                void redirectsTo_form() throws Exception {
                    Long id = magazine.getId();
                    mockMvc.perform(
                            put("/magazines/" + id)
                                    .with(csrf())
                                    .param("id", "")
                                    .param("title", "")
                                    .param("shortExplanation", "")
                                    .param("longExplanation", "")
                                    .param("policy", "")
                    )
                            .andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrl("/magazines/" + id + "/form"))
                    ;
                }
            }

            @Nested
            class With_stranger {
                @Test
                void handles_exception() throws Exception {
                    logout();
                    Account stranger = factoryFacade.createAccount("stranger");
                    login(stranger);

                    Long id = magazine.getId();
                    mockMvc.perform(
                            put("/magazines/" + id)
                                    .with(csrf())
                                    .param("id", id.toString())
                                    .param("title", "Updated")
                                    .param("shortExplanation", "Updated short.")
                                    .param("longExplanation", "Updated long.")
                                    .param("policy", "OPEN")
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

        Magazine magazine;

        @BeforeEach
        void setup() {
            magazine = factoryFacade.createMagazine(manager, "Test Magazine", Policy.OPEN);
        }

        @Test
        void updates_list_And_redirectsTo_home() throws Exception {
            mockMvc.perform(
                    delete("/magazines/" + magazine.getId())
                            .with(csrf())
            )
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/"))
            ;
            assertThrows(MagazineNotFoundException.class, () -> magazineService.find(magazine.getId()));
            List<MagazineSimpleDto> bestList = bestListCache.getBestMagazines();
            assertThat(bestList).isEmpty();
        }

        @Nested
        class When_hasChild {
            @Test
            void handles_exception() throws Exception {
                factoryFacade.createTopic(magazine, manager, "Test topic");
                mockMvc.perform(
                        delete("/magazines/" + magazine.getId())
                                .with(csrf())
                )
                        .andExpect(status().isOk())
                        .andExpect(view().name("error/not_deletable"))
                ;
                assertDoesNotThrow(() -> magazineService.find(magazine.getId()));
            }
        }

        @Nested
        class With_stranger {
            @Test
            void handles_exception() throws Exception {
                logout();
                Account stranger = factoryFacade.createAccount("stranger");
                login(stranger);

                mockMvc.perform(
                        delete("/magazines/" + magazine.getId())
                                .with(csrf())
                )
                        .andExpect(status().isOk())
                        .andExpect(view().name("error/access"))
                ;
                assertDoesNotThrow(() -> magazineService.find(magazine.getId()));
            }
        }

    }

    @Nested
    class Member {

        Magazine managed;
        Account member;

        @BeforeEach
        void setup() {
            managed = factoryFacade.createMagazine(manager, "Managed Magazine", Policy.MANAGED);
            member = factoryFacade.createAccount("member");
        }

        @Nested
        class With_member {

            @BeforeEach
            void setup() {
                logout();
                login(member);
            }

            @Nested
            class Apply {
                @Test
                void sends_And_redirectsTo_sent() throws Exception {
                    mockMvc.perform(
                            post("/magazines/" + managed.getId() + "/members")
                                    .with(csrf())
                    )
                            .andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrl("/sent-to-manager?magazineId=" + managed.getId()))
                    ;
                    MemberAppliedEvent event = new MemberAppliedEvent(managed.getId(), managed.getTitle(), manager.getUsername(), member.getUsername());
                    then(notificationListener).should().onMemberAppliedEvent(event);
                }
            }

            @Nested
            class Quit {
                @Test
                void quits_sends_And_redirectsTo_Magazine() throws Exception {
                    magazineService.addMember(managed.getId(), manager.getUsername(), member.getUsername());
                    mockMvc.perform(
                            delete("/magazines/" + managed.getId() + "/members")
                                    .with(csrf())
                    )
                            .andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrl("/magazines/" + managed.getId()))
                    ;
                    Thread.sleep(1000L);
                    List<String> members = magazineService.load(managed.getTitle()).getMembers();
                    assertThat(members).doesNotContain(member.getUsername());
                    MemberQuitEvent event = new MemberQuitEvent(managed.getId(), managed.getTitle(), manager.getUsername(), member.getUsername());
                    then(notificationListener).should().onMemberQuitEvent(event);
                }
            }

        }

        @Nested
        class With_manager {

            @Nested
            class Page {

                @Test
                void returns_members() throws Exception {
                    mockMvc.perform(
                            get("/magazines/" + managed.getId() + "/members")
                    )
                            .andExpect(status().isOk())
                            .andExpect(view().name("page/board/magazine/members"))
                            .andExpect(model().attributeExists("members"))
                    ;
                }
            }

            @Nested
            class Add {
                @Test
                void redirectsTo_magazine() throws Exception {
                    mockMvc.perform(
                            get("/magazines/" + managed.getId() + "/" + member.getUsername())
                                    .with(csrf())
                    )
                            .andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrl("/magazines/" + managed.getId() + "/members"))
                    ;
                    Thread.sleep(1000L);
                    List<String> members = magazineService.load(managed.getTitle()).getMembers();
                    assertThat(members).contains(member.getUsername());
                    MemberAddedEvent event = new MemberAddedEvent(managed.getId(), managed.getTitle(), member.getUsername());
                    then(notificationListener).should().onMemberAddedEvent(event);
                }

                @Nested
                class ByLink {
                    @Test
                    void shows_added() throws Exception {
                        mockMvc.perform(
                                get("/magazines/" + managed.getId() + "/" + member.getUsername())
                                        .with(csrf())
                        )
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/magazines/" + managed.getId() + "/members"))
                        ;
                        Thread.sleep(1000L);
                        List<String> members = magazineService.load(managed.getTitle()).getMembers();
                        assertThat(members).contains(member.getUsername());
                        MemberAddedEvent event = new MemberAddedEvent(managed.getId(), managed.getTitle(), member.getUsername());
                        then(notificationListener).should().onMemberAddedEvent(event);
                    }
                }
            }

            @Nested
            class Remove {
                @Test
                void redirectsTo_magazine() throws Exception {
                    magazineService.addMember(managed.getId(), manager.getUsername(), member.getUsername());
                    mockMvc.perform(
                            delete("/magazines/" + managed.getId() + "/" + member.getUsername())
                                    .with(csrf())
                    )
                            .andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrl("/magazines/" + managed.getId() + "/members"))
                    ;
                    Thread.sleep(1000L);
                    List<String> members = magazineService.load(managed.getTitle()).getMembers();
                    assertThat(members).doesNotContain(member.getUsername());
                    MemberRemovedEvent event = new MemberRemovedEvent(managed.getId(), managed.getTitle(), member.getUsername());
                    then(notificationListener).should().onMemberRemovedEvent(event);
                }
            }

        }

    }

    @Nested
    class AllMagazines {

        @Test
        void shows_magazines() throws Exception {
            factoryFacade.createMagazine(manager, "Magazine 1", Policy.OPEN);
            factoryFacade.createMagazine(manager, "Magazine 2", Policy.OPEN);
            factoryFacade.createMagazine(manager, "Magazine 3", Policy.OPEN);
            mockMvc.perform(
                    get("/magazines")
            )
                    .andExpect(status().isOk())
                    .andExpect(view().name("page/board/magazine/list"))
                    .andExpect(model().attributeExists("magazines"))
            ;
        }

    }

    @Nested
    class Api {

        @Nested
        class Best {
            @Test
            void returns_magazines() throws Exception {
                factoryFacade.createMagazine(manager, "Magazine 1", Policy.OPEN);
                factoryFacade.createMagazine(manager, "Magazine 2", Policy.OPEN);
                factoryFacade.createMagazine(manager, "Magazine 3", Policy.OPEN);
                factoryFacade.createMagazine(manager, "Magazine 4", Policy.OPEN);
                factoryFacade.createMagazine(manager, "Magazine 5", Policy.OPEN);
                factoryFacade.createMagazine(manager, "Magazine 6", Policy.OPEN);
                mockMvc.perform(
                        get("/api/magazines")
                )
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(5)))
                        .andExpect(jsonPath("[0]").exists())
                        .andExpect(jsonPath("[1]").exists())
                        .andExpect(jsonPath("[2]").exists())
                        .andExpect(jsonPath("[3]").exists())
                        .andExpect(jsonPath("[4]").exists())
                        .andExpect(jsonPath("[5]").doesNotExist())
                        .andExpect(jsonPath("[0].*", hasSize(2)))
                        .andExpect(jsonPath("[0].id").exists())
                        .andExpect(jsonPath("[0].title").exists())
                        .andExpect(jsonPath("[0].manager").doesNotExist())
                ;
            }
        }

        @Nested
        class OfUser {
            @Test
            void returns_list() throws Exception {
                factoryFacade.createMagazine(manager, "Manager's", Policy.OPEN);
                Account user = factoryFacade.createAccount("user");
                factoryFacade.createMagazine(user, "User's 1", Policy.OPEN);
                factoryFacade.createMagazine(user, "User'2 2", Policy.OPEN);
                mockMvc.perform(
                        get("/api/magazines/" + user.getUsername())
                )
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(2)))
                        .andExpect(jsonPath("[0]").exists())
                        .andExpect(jsonPath("[1]").exists())
                        .andExpect(jsonPath("[2]").doesNotExist())
                ;
            }
        }

    }

}
