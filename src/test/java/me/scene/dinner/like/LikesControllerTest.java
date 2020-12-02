package me.scene.dinner.like;

import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.article.domain.Status;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.magazine.domain.Policy;
import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.notification.NotificationListener;
import me.scene.dinner.test.facade.FactoryFacade;
import me.scene.dinner.test.facade.RepositoryFacade;
import me.scene.dinner.test.proxy.service.ArticleServiceProxy;
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
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Like")
class LikesControllerTest {

    @Autowired MockMvc mockMvc;
    @SpyBean NotificationListener notificationListener;

    @Autowired LikesService likesService;
    @SpyBean ArticleServiceProxy articleService;

    @Autowired FactoryFacade factoryFacade;
    @Autowired RepositoryFacade repositoryFacade;


    Account writer;
    Magazine magazine;
    Topic topic;
    Article article;
    Account reader;

    @BeforeEach
    void setup() {
        writer = factoryFacade.createAccount("writer");
        magazine = factoryFacade.createMagazine(writer, "Test Magazine", Policy.OPEN);
        topic = factoryFacade.createTopic(magazine, writer, "Test Topic");
        article = factoryFacade.createArticle(topic, writer, "Test Article", Status.PUBLIC);
        reader = factoryFacade.createAccount("reader");
        login(reader);
    }

    @AfterEach
    void clear() {
        repositoryFacade.deleteAll();
        reset(notificationListener);
    }


    @Nested
    class Likes {

        @Test
        void adds_likes() throws Exception {
            mockMvc.perform(
                    post("/api/likes")
                            .with(csrf())
                            .param("username", reader.getUsername())
                            .param("articleId", article.getId().toString())
            )
                    .andExpect(status().isOk())
            ;
            article = articleService.find(article.getId());
            assertThat(article.getLikes()).isEqualTo(1);
            LikedEvent event = new LikedEvent(writer.getUsername(), reader.getUsername(), article.getTitle());
            then(notificationListener).should().onLikedEvent(event);
        }

        @Test
        void removes_likes() throws Exception {
            likesService.likes(reader.getUsername(), article.getId());
            mockMvc.perform(
                    delete("/api/likes")
                            .with(csrf())
                            .param("username", reader.getUsername())
                            .param("articleId", article.getId().toString())
            )
                    .andExpect(status().isOk())
            ;
            article = articleService.find(article.getId());
            assertThat(article.getLikes()).isEqualTo(0);
        }

        @Nested
        class When_unauthenticated {
            @Test
            void redirectsTo_login() throws Exception {
                logout();
                mockMvc.perform(
                        post("/api/likes")
                                .with(csrf())
                )
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrlPattern("**/login"))
                ;
            }
        }

    }

    @Nested
    class DoesLike {

        @Test
        void returns_boolean() throws Exception {
            mockMvc.perform(
                    get("/api/likes")
                            .with(csrf())
                            .param("username", reader.getUsername())
                            .param("articleId", article.getId().toString())
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(false))
            ;

            likesService.likes(reader.getUsername(), article.getId());
            mockMvc.perform(
                    get("/api/likes")
                            .with(csrf())
                            .param("username", reader.getUsername())
                            .param("articleId", article.getId().toString())
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(true))
            ;
        }

    }

}