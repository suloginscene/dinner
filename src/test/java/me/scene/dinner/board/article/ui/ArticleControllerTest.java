package me.scene.dinner.board.article.ui;

import me.scene.dinner.account.domain.Account;
import me.scene.dinner.account.domain.AccountRepository;
import me.scene.dinner.board.article.application.ArticleService;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.article.domain.ArticleRepository;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.utils.authentication.WithAccount;
import me.scene.dinner.utils.factory.AccountFactory;
import me.scene.dinner.utils.factory.MagazineFactory;
import me.scene.dinner.utils.factory.TopicFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class ArticleControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountFactory accountFactory;
    @Autowired AccountRepository accountRepository;
    @Autowired MagazineFactory magazineFactory;
    @Autowired TopicFactory topicFactory;
    @Autowired ArticleService articleService;
    @Autowired ArticleRepository articleRepository;

    @Test
    @WithAccount(username = "scene")
    void createPage_hasForm() throws Exception {
        mockMvc.perform(
                get("/article-form")
                        .param("topicId", "1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/board/article/form"))
                .andExpect(model().attributeExists("articleForm"))
        ;
    }

    @Test
    void createPage_unauthenticated_beGuidedBySpringSecurity() throws Exception {
        mockMvc.perform(
                get("/article-form")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void create_saveAndShow() throws Exception {
        Account account = accountFactory.create("magazineManager", "magazine_manager@email.com", "password");
        Magazine magazine = magazineFactory.create(account.getUsername(), "title", "short", "long", "OPEN");
        Topic topic = topicFactory.create(magazine.getId(), account.getUsername(), "title", "short", "long");

        mockMvc.perform(
                post("/articles")
                        .param("topicId", topic.getId().toString())
                        .param("title", "Test Article")
                        .param("content", "This is test article.")
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/articles/*"))
        ;
        Article article = articleRepository.findByTitle("Test Article").orElseThrow();
        assertThat(article.getContent()).isEqualTo("This is test article.");
        assertThat(article.getTopic()).isEqualTo(topic);
        assertThat(article.getWriter()).isEqualTo(accountRepository.findByUsername("scene").orElseThrow().getUsername());
    }

    @Test
    @WithAccount(username = "scene")
    void create_invalidParam_returnErrors() throws Exception {
        mockMvc.perform(
                post("/articles")
                        .with(csrf())
                        .param("topicId", "1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/board/article/form"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(2))
        ;
    }

    @Test
    void create_unauthenticated_beGuidedBySpringSecurity() throws Exception {
        mockMvc.perform(
                post("/articles").with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void create_unauthorized_handleException() throws Exception {
        Account account = accountFactory.create("magazineManager", "magazine_manager@email.com", "password");

        Magazine exclusive = magazineFactory.create(account.getUsername(), "title", "short", "long", "EXCLUSIVE");
        Topic exclusiveTopic = topicFactory.create(exclusive.getId(), account.getUsername(), "title", "short", "long");
        mockMvc.perform(
                post("/articles")
                        .param("topicId", exclusiveTopic.getId().toString())
                        .param("title", "Test Article")
                        .param("content", "This is test article.")
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/authorization"))
        ;

        Magazine managed = magazineFactory.create(account.getUsername(), "title", "short", "long", "MANAGED");
        Topic managedTopic = topicFactory.create(managed.getId(), account.getUsername(), "title", "short", "long");
        mockMvc.perform(
                post("/articles")
                        .param("topicId", managedTopic.getId().toString())
                        .param("title", "Test Article")
                        .param("content", "This is test article.")
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/authorization"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void create_exclusiveByManager_success() throws Exception {
        Account scene = accountRepository.findByUsername("scene").orElseThrow();
        Magazine exclusive = magazineFactory.create(scene.getUsername(), "title", "short", "long", "EXCLUSIVE");
        Topic topic = topicFactory.create(exclusive.getId(), scene.getUsername(), "title", "short", "long");

        mockMvc.perform(
                post("/articles")
                        .param("topicId", topic.getId().toString())
                        .param("title", "Test Article")
                        .param("content", "This is test article.")
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/articles/*"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void create_managedByAuthorized_success() throws Exception {
        Account account = accountFactory.create("magazineManager", "magazine_manager@email.com", "password");
        Magazine managed = magazineFactory.create(account.getUsername(), "title", "short", "long", "MANAGED");
        Topic topic = topicFactory.create(managed.getId(), account.getUsername(), "title", "short", "long");
        managed.registerAsAuthorizedWriter("scene");

        mockMvc.perform(
                post("/articles")
                        .param("topicId", topic.getId().toString())
                        .param("title", "Test Article")
                        .param("content", "This is test article.")
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/articles/*"))
        ;
    }

    @Test
    void show_hasArticle() throws Exception {
        Account account = accountFactory.create("scene", "scene@email.com", "password");
        Magazine magazine = magazineFactory.create(account.getUsername(), "title", "short", "long", "OPEN");
        Topic topic = topicFactory.create(magazine.getId(), account.getUsername(), "title", "short", "long");
        Long id = articleService.save(topic.getId(), account.getUsername(), "title", "content");
        articleService.publish(id, account.getUsername());

        mockMvc.perform(
                get("/articles/" + id)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/board/article/view"))
                .andExpect(model().attributeExists("article"))
        ;
    }

    @Test
    void show_nonExistent_handleException() throws Exception {
        Account account = accountFactory.create("scene", "scene@email.com", "password");
        Magazine magazine = magazineFactory.create(account.getUsername(), "title", "short", "long", "OPEN");
        Topic topic = topicFactory.create(magazine.getId(), account.getUsername(), "title", "short", "long");
        Long id = articleService.save(topic.getId(), account.getUsername(), "title", "content");

        mockMvc.perform(
                get("/articles/" + (id + 1))
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/board_not_found"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void unpublished_byWriter_hasArticle() throws Exception {
        Magazine magazine = magazineFactory.create("scene", "title", "short", "long", "OPEN");
        Topic topic = topicFactory.create(magazine.getId(), "scene", "title", "short", "long");
        Long id = articleService.save(topic.getId(), "scene", "title", "content");

        mockMvc.perform(
                get("/articles/" + id)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/board/article/view"))
                .andExpect(model().attributeExists("article"))
        ;
    }

    @Test
    void unpublished_byStranger_handleException() throws Exception {
        Account account = accountFactory.create("scene", "scene@email.com", "password");
        Magazine magazine = magazineFactory.create(account.getUsername(), "title", "short", "long", "OPEN");
        Topic topic = topicFactory.create(magazine.getId(), account.getUsername(), "title", "short", "long");
        Long id = articleService.save(topic.getId(), account.getUsername(), "title", "content");

        mockMvc.perform(
                get("/articles/" + id)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/publication"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void publish_published() throws Exception {
        Magazine magazine = magazineFactory.create("scene", "title", "short", "long", "OPEN");
        Topic topic = topicFactory.create(magazine.getId(), "scene", "title", "short", "long");
        Long id = articleService.save(topic.getId(), "scene", "title", "content");

        Article unpublished = articleService.find(id, "scene");
        assertThat(unpublished.isPublished()).isFalse();
        assertThat(magazine.getWriters()).doesNotContain("scene");

        mockMvc.perform(
                post("/articles/" + id)
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles/" + id))
        ;
        Article published = articleService.find(id, "scene");
        assertThat(published.isPublished()).isTrue();
        assertThat(magazine.getWriters()).contains("scene");
    }

    @Test
    @WithAccount(username = "stranger")
    void publish_byStranger_handleException() throws Exception {
        Account account = accountFactory.create("scene", "scene@email.com", "password");
        Magazine magazine = magazineFactory.create(account.getUsername(), "title", "short", "long", "OPEN");
        Topic topic = topicFactory.create(magazine.getId(), account.getUsername(), "title", "short", "long");
        Long id = articleService.save(topic.getId(), account.getUsername(), "title", "content");

        mockMvc.perform(
                post("/articles/" + id)
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/publication"))
        ;
        Article article = articleService.find(id, "scene");
        assertThat(article.isPublished()).isFalse();
    }

}
