package me.scene.dinner.board.reply.ui;

import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.article.domain.ArticleRepository;
import me.scene.dinner.board.article.domain.Status;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.magazine.domain.MagazineRepository;
import me.scene.dinner.board.reply.application.ReplyService;
import me.scene.dinner.board.reply.domain.Reply;
import me.scene.dinner.board.reply.domain.ReplyRepository;
import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.board.topic.domain.TopicRepository;
import me.scene.dinner.test.utils.authentication.WithAccount;
import me.scene.dinner.test.factory.AccountFactory;
import me.scene.dinner.test.factory.ArticleFactory;
import me.scene.dinner.test.factory.MagazineFactory;
import me.scene.dinner.test.factory.TopicFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class ReplyControllerTest {

    @Autowired MockMvc mockMvc;

    @Autowired ReplyService replyService;

    @Autowired AccountFactory accountFactory;
    @Autowired MagazineFactory magazineFactory;
    @Autowired TopicFactory topicFactory;
    @Autowired ArticleFactory articleFactory;

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
    @Transactional
    @WithAccount(username = "scene")
    void create_saveAndShowArticle() throws Exception {
        Account account = accountFactory.create("magazineManager", "magazine_manager@email.com", "password");
        Magazine magazine = magazineFactory.create(account.getUsername(), account.getEmail(), "title", "short", "long", "OPEN");
        Topic topic = topicFactory.create(magazine.getId(), account.getUsername(), "title", "short", "long");
        Article article = articleFactory.create(topic.getId(), account.getUsername(), "title", "content", Status.PUBLIC.name());

        mockMvc.perform(
                post("/replies")
                        .param("articleId", article.getId().toString())
                        .param("content", "This is test reply.")
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/articles/*"))
        ;
        assertThat(article.getReplies()).anyMatch((r) -> r.getContent().equals("This is test reply."));
    }

    @Test
    void create_unauthenticated_beGuidedBySpringSecurity() throws Exception {
        mockMvc.perform(
                post("/replies").with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"))
        ;
    }

    @Test
    @Transactional
    void show_hasReply() throws Exception {
        Account account = accountFactory.create("scene", "scene@email.com", "password");
        Magazine magazine = magazineFactory.create(account.getUsername(), account.getEmail(), "title", "short", "long", "OPEN");
        Topic topic = topicFactory.create(magazine.getId(), account.getUsername(), "title", "short", "long");
        Article article = articleFactory.create(topic.getId(), account.getUsername(), "title", "content", Status.PUBLIC.name());
        replyService.save(article.getId(), account.getUsername(), "This is test reply.");

        mockMvc.perform(
                get("/articles/" + article.getId())
        )
                .andExpect(status().isOk())
                .andExpect(model().attribute("article", article))
        ;
        assertThat(article.getReplies()).anyMatch((r) -> r.getContent().equals("This is test reply."));
    }

    @Test
    @Transactional
    @WithAccount(username = "scene")
    void delete_deleted() throws Exception {
        Magazine magazine = magazineFactory.create("scene", "email@email.com", "title", "short", "long", "OPEN");
        Topic topic = topicFactory.create(magazine.getId(), "scene", "title", "short", "long");
        Article article = articleFactory.create(topic.getId(), "scene", "title", "content", Status.PUBLIC.name());
        replyService.save(article.getId(), "scene", "content");
        List<Reply> replies = replyRepository.findAll();
        if (replies.size() != 1) fail("Illegal Test State");
        Long id = replies.get(0).getId();

        mockMvc.perform(
                delete("/replies/" + id)
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles/" + article.getId()))
        ;
        assertThrows(NoSuchElementException.class, () -> replyRepository.findById(id).orElseThrow());
        assertThat(article.getReplies().size()).isEqualTo(0);
    }

    @Test
    @WithAccount(username = "scene")
    void delete_byStranger_handleException() throws Exception {
        Account account = accountFactory.create("magazineManager", "manager@email.com", "password");
        Magazine magazine = magazineFactory.create(account.getUsername(), account.getEmail(), "title", "short", "long", "OPEN");
        Topic topic = topicFactory.create(magazine.getId(), account.getUsername(), "title", "short", "long");
        Article article = articleFactory.create(topic.getId(), account.getUsername(), "title", "content", Status.PUBLIC.name());
        replyService.save(article.getId(), account.getUsername(), "content");
        List<Reply> replies = replyRepository.findAll();
        if (replies.size() != 1) fail("Illegal Test State");
        Long id = replies.get(0).getId();

        mockMvc.perform(
                delete("/replies/" + id)
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/access"))
        ;
        assertDoesNotThrow(() -> replyRepository.findById(id).orElseThrow());
    }

}
