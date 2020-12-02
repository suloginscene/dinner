package me.scene.dinner.notification;

import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.article.domain.Status;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.magazine.domain.Policy;
import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.like.LikesService;
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
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static me.scene.dinner.test.utils.Authenticators.login;
import static me.scene.dinner.test.utils.Authenticators.logout;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Notification")
class NotificationControllerTest {

    @Autowired MockMvc mockMvc;

    @Autowired NotificationService notificationService;
    @Autowired LikesService likesService;

    @Autowired FactoryFacade factoryFacade;
    @Autowired RepositoryFacade repositoryFacade;


    Account user;
    Account reader;
    Magazine magazine;
    Topic topic;
    Article article;

    @BeforeEach
    void setup() {
        user = factoryFacade.createAccount("user");
        reader = factoryFacade.createAccount("reader");
        magazine = factoryFacade.createMagazine(user, "Test Magazine", Policy.OPEN);
        topic = factoryFacade.createTopic(magazine, user, "Test Topic");
        article = factoryFacade.createArticle(topic, user, "Test Article", Status.PUBLIC);
        login(user);
    }

    @AfterEach
    void clear() {
        repositoryFacade.deleteAll();
    }


    @Nested
    class Page {

        List<Notification> unchecked;
        List<Notification> checked;

        @Test
        void returns_notifications() throws Exception {
            likesService.likes(user.getUsername(), article.getId());
            Thread.sleep(1000L);
            mockMvc.perform(
                    get("/notifications")
            )
                    .andExpect(status().isOk())
                    .andExpect(view().name("page/notification/view"))
                    .andExpect(model().attribute("uncheckedList", hasSize(1)))
                    .andExpect(model().attribute("checkedList", hasSize(0)))
            ;
            unchecked = notificationService.findUnchecked(user.getUsername());
            assertThat(unchecked).isEmpty();
            checked = notificationService.findChecked(user.getUsername());
            assertThat(checked).hasSize(1);

            likesService.likes(reader.getUsername(), article.getId());
            Thread.sleep(1000L);
            mockMvc.perform(
                    get("/notifications")
            )
                    .andExpect(status().isOk())
                    .andExpect(view().name("page/notification/view"))
                    .andExpect(model().attribute("uncheckedList", hasSize(1)))
                    .andExpect(model().attribute("checkedList", hasSize(1)))
            ;
            unchecked = notificationService.findUnchecked(user.getUsername());
            assertThat(unchecked).isEmpty();
            checked = notificationService.findChecked(user.getUsername());
            assertThat(checked).hasSize(2);
        }

        @Nested
        class When_unauthenticated {
            @Test
            void redirectsTo_login() throws Exception {
                logout();
                mockMvc.perform(
                        get("/notifications")
                )
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrlPattern("**/login"))
                ;
            }
        }

    }

    @Nested
    class Interceptor {

        @Test
        void returns_count() throws Exception {
            likesService.likes(reader.getUsername(), article.getId());
            Thread.sleep(1000L);
            mockMvc.perform(
                    get("/")
            )
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("notificationCount", is(1L)))
            ;
        }

    }

}
