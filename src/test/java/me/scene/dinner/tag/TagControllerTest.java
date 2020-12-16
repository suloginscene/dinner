package me.scene.dinner.tag;

import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.magazine.domain.Policy;
import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.tag.application.dto.ArticleSummary;
import me.scene.dinner.tag.application.dto.TagDto;
import me.scene.dinner.tag.application.TagService;
import me.scene.dinner.tag.domain.Tag;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Tag")
class TagControllerTest {

    @Autowired MockMvc mockMvc;

    @Autowired TagService tagService;

    @Autowired FactoryFacade factoryFacade;
    @Autowired RepositoryFacade repositoryFacade;


    Account user;
    Tag tag;

    @BeforeEach
    void setup() {
        tag = new Tag("Test");
        tagService.save(tag.getName());
        user = factoryFacade.createAccount("user");
        login(user);
    }

    @AfterEach
    void clear() {
        repositoryFacade.deleteAll();
    }


    @Nested
    class ListPage {

        @Test
        void returns_tags() throws Exception {
            mockMvc.perform(
                    get("/tags")
            )
                    .andExpect(status().isOk())
                    .andExpect(view().name("page/tag/list"))
                    .andExpect(model().attribute("tags", hasSize(1)))
            ;
        }

    }

    @Nested
    class Create {

        @Test
        void create_tag() throws Exception {
            String newTag = "new";
            mockMvc.perform(
                    post("/api/tags/" + newTag)
                            .with(csrf())
            )
                    .andExpect(status().isOk())
            ;
            List<String> tags = tagService.findAllTagNames();
            assertThat(tags).contains(newTag);
        }

    }

    @Nested
    class Delete {

        @Test
        void delete_tag() throws Exception {
            mockMvc.perform(
                    delete("/api/tags/" + tag.getName())
                            .with(csrf())
            )
                    .andExpect(status().isOk())
            ;
            List<String> tags = tagService.findAllTagNames();
            assertThat(tags).isEmpty();
        }

        @Nested
        class OnReferred {
            @Test
            void not_delete() throws Exception {
                Magazine magazine = factoryFacade.createMagazine(user, "Magazine", Policy.OPEN);
                Topic topic = factoryFacade.createTopic(magazine, user, "Topic");
                factoryFacade.createArticle(topic, user, "Article", true, tag.getName());
                mockMvc.perform(
                        delete("/api/tags/" + tag.getName())
                                .with(csrf())
                )
                        .andExpect(status().isOk())
                ;
                List<String> tags = tagService.findAllTagNames();
                assertThat(tags).isNotEmpty();
            }
        }

    }

    @Nested
    class Page {

        Article article;

        @BeforeEach
        void setup() {
            Magazine magazine = factoryFacade.createMagazine(user, "Magazine", Policy.OPEN);
            Topic topic = factoryFacade.createTopic(magazine, user, "Topic");
            article = factoryFacade.createArticle(topic, user, "Article", true, tag.getName());
        }

        @Test
        void show_taggedArticles() throws Exception {
            TagDto expected = new TagDto(tag.getName(), List.of(new ArticleSummary(article.getId(), article.getTitle())));
            mockMvc.perform(
                    get("/tags/" + tag.getName())
            )
                    .andExpect(status().isOk())
                    .andExpect(view().name("page/tag/view"))
                    .andExpect(model().attributeExists("tag"))
                    .andExpect(model().attribute("tag", is(expected)));
        }

    }

}
