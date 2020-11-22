package me.scene.dinner.board.article.ui;

import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.board.article.application.ArticleService;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.article.domain.ArticleRepository;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.magazine.domain.MagazineRepository;
import me.scene.dinner.board.reply.application.ReplyService;
import me.scene.dinner.board.reply.domain.Reply;
import me.scene.dinner.board.reply.domain.ReplyRepository;
import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.board.topic.domain.TopicRepository;
import me.scene.dinner.common.exception.BoardNotFoundException;
import me.scene.dinner.utils.authentication.WithAccount;
import me.scene.dinner.utils.factory.AccountFactory;
import me.scene.dinner.utils.factory.MagazineFactory;
import me.scene.dinner.utils.factory.TopicFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
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
class ArticleControllerTest {

    @Autowired MockMvc mockMvc;

    @Autowired ArticleService articleService;

    @Autowired AccountFactory accountFactory;
    @Autowired MagazineFactory magazineFactory;
    @Autowired TopicFactory topicFactory;
    @Autowired ReplyService replyService;

    @Autowired AccountRepository accountRepository;
    @Autowired MagazineRepository magazineRepository;
    @Autowired TopicRepository topicRepository;
    @Autowired ArticleRepository articleRepository;
    @Autowired ReplyRepository replyRepository;

    @AfterEach
    void clear() {
        accountRepository.deleteAll();
        replyRepository.deleteAll();
        articleRepository.deleteAll();
        topicRepository.deleteAll();
        magazineRepository.deleteAll();
    }

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
    @Transactional
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
                .andExpect(view().name("error/access"))
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
                .andExpect(view().name("error/access"))
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
    @Transactional
    @WithAccount(username = "scene")
    void create_managedByAuthorized_success() throws Exception {
        Account account = accountFactory.create("magazineManager", "magazine_manager@email.com", "password");
        Magazine managed = magazineFactory.create(account.getUsername(), "title", "short", "long", "MANAGED");
        Topic topic = topicFactory.create(managed.getId(), account.getUsername(), "title", "short", "long");
        managed.addMember("magazineManager", "scene");

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
    @Transactional
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
    @Transactional
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
                .andExpect(view().name("error/access"))
        ;
    }

    @Test
    @Transactional
    @WithAccount(username = "scene")
    void publish_published() throws Exception {
        Magazine magazine = magazineFactory.create("scene", "title", "short", "long", "OPEN");
        Topic topic = topicFactory.create(magazine.getId(), "scene", "title", "short", "long");
        Long id = articleService.save(topic.getId(), "scene", "title", "content");

        Article unpublished = articleService.find(id);
        assertThat(unpublished.isPublished()).isFalse();
        assertThat(magazine.getWriters()).doesNotContain("scene");

        mockMvc.perform(
                post("/articles/" + id)
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles/" + id))
        ;
        Article published = articleService.find(id);
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
                .andExpect(view().name("error/access"))
        ;
        Article article = articleService.find(id);
        assertThat(article.isPublished()).isFalse();
    }

    @Test
    @WithAccount(username = "scene")
    void updatePage_hasForm() throws Exception {
        Magazine magazine = magazineFactory.create("scene", "title", "short", "long", "OPEN");
        Topic topic = topicFactory.create(magazine.getId(), "scene", "title", "short", "long");
        Long id = articleService.save(topic.getId(), "scene", "title", "content");

        mockMvc.perform(
                get("/articles/" + id + "/form")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/board/article/update"))
                .andExpect(model().attributeExists("updateForm"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void updatePage_byStranger_handleException() throws Exception {
        Account account = accountFactory.create("magazineManager", "manager@email.com", "password");
        Magazine magazine = magazineFactory.create(account.getUsername(), "title", "short", "long", "OPEN");
        Topic topic = topicFactory.create(magazine.getId(), account.getUsername(), "title", "short", "long");
        Long id = articleService.save(topic.getId(), account.getUsername(), "title", "content");

        mockMvc.perform(
                get("/articles/" + id + "/form")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/access"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void update_updated() throws Exception {
        Magazine magazine = magazineFactory.create("scene", "title", "short", "long", "OPEN");
        Topic topic = topicFactory.create(magazine.getId(), "scene", "title", "short", "long");
        Long id = articleService.save(topic.getId(), "scene", "title", "content");

        mockMvc.perform(
                put("/articles/" + id)
                        .with(csrf())
                        .param("id", id.toString())
                        .param("topicId", topic.getId().toString())
                        .param("title", "Updated")
                        .param("content", "Updated content.")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles/" + id))
        ;

        Article article = articleService.find(id);
        assertThat(article.getTitle()).isEqualTo("Updated");
        assertThat(article.getContent()).isEqualTo("Updated content.");
    }

    @Test
    @WithAccount(username = "scene")
    void update_byStranger_handleException() throws Exception {
        Account account = accountFactory.create("magazineManager", "manager@email.com", "password");
        Magazine magazine = magazineFactory.create(account.getUsername(), "title", "short", "long", "OPEN");
        Topic topic = topicFactory.create(magazine.getId(), account.getUsername(), "title", "short", "long");
        Long id = articleService.save(topic.getId(), account.getUsername(), "title", "content");

        mockMvc.perform(
                put("/articles/" + id)
                        .with(csrf())
                        .param("id", id.toString())
                        .param("topicId", topic.getId().toString())
                        .param("title", "Updated")
                        .param("content", "Updated content.")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/access"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void update_invalidParam_redirected() throws Exception {
        Magazine magazine = magazineFactory.create("scene", "title", "short", "long", "OPEN");
        Topic topic = topicFactory.create(magazine.getId(), "scene", "title", "short", "long");
        Long id = articleService.save(topic.getId(), "scene", "title", "content");

        mockMvc.perform(
                put("/articles/" + id)
                        .with(csrf())
                        .param("id", "")
                        .param("topicId", "")
                        .param("title", "")
                        .param("content", "")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles/" + id + "/form"))
        ;
    }

    @Test
    @Transactional
    @WithAccount(username = "scene")
    void delete_deleted() throws Exception {
        Magazine magazine = magazineFactory.create("scene", "title", "short", "long", "OPEN");
        Topic topic = topicFactory.create(magazine.getId(), "scene", "title", "short", "long");
        Long id = articleService.save(topic.getId(), "scene", "title", "content");
        articleService.publish(id, "scene");
        if (!magazine.getWriters().contains("scene")) fail("Illegal Test State");

        mockMvc.perform(
                delete("/articles/" + id)
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/topics/" + topic.getId()))
        ;
        assertThrows(BoardNotFoundException.class, () -> articleService.find(id));
        assertThat(topic.getArticles().size()).isEqualTo(0);
        assertThat(magazine.getWriters()).doesNotContain("scene");
    }

    @Test
    @WithAccount(username = "scene")
    void delete_byStranger_handleException() throws Exception {
        Account account = accountFactory.create("magazineManager", "manager@email.com", "password");
        Magazine magazine = magazineFactory.create(account.getUsername(), "title", "short", "long", "OPEN");
        Topic topic = topicFactory.create(magazine.getId(), account.getUsername(), "title", "short", "long");
        Long id = articleService.save(topic.getId(), account.getUsername(), "title", "content");

        mockMvc.perform(
                delete("/articles/" + id)
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/access"))
        ;
        assertDoesNotThrow(() -> articleService.find(id));
    }

    @Test
    @WithAccount(username = "scene")
    void delete_hasReply_removeRecursivelyByDomainEvent() throws Exception {
        Magazine magazine = magazineFactory.create("scene", "title", "short", "long", "OPEN");
        Topic topic = topicFactory.create(magazine.getId(), "scene", "title", "short", "long");
        Long targetArticleId = articleService.save(topic.getId(), "scene", "title", "content");
        replyService.save(targetArticleId, "replyWriter", "it will be deleted");

        Long anotherArticleId = articleService.save(topic.getId(), "scene", "title", "content");
        replyService.save(anotherArticleId, "replyWriter", "it will survive");
        if (replyRepository.findAll().size() != 2) fail("Illegal Test State");

        mockMvc.perform(
                delete("/articles/" + targetArticleId)
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/topics/" + topic.getId()))
        ;
        assertThrows(BoardNotFoundException.class, () -> articleService.find(targetArticleId));

        List<Reply> replies = replyRepository.findAll();
        assertThat(replies.size()).isEqualTo(1);
        assertThat(replies.get(0).getContent()).isEqualTo("it will survive");
    }

}
