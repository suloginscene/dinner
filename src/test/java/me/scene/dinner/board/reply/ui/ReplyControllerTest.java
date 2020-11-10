package me.scene.dinner.board.reply.ui;

import me.scene.dinner.account.domain.Account;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.reply.application.ReplyService;
import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.utils.authentication.WithAccount;
import me.scene.dinner.utils.factory.AccountFactory;
import me.scene.dinner.utils.factory.ArticleFactory;
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
class ReplyControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountFactory accountFactory;
    @Autowired MagazineFactory magazineFactory;
    @Autowired TopicFactory topicFactory;
    @Autowired ArticleFactory articleFactory;
    @Autowired ReplyService replyService;

    @Test
    @WithAccount(username = "scene")
    void create_saveAndShowArticle() throws Exception {
        Account account = accountFactory.create("magazineManager", "magazine_manager@email.com", "password");
        Magazine magazine = magazineFactory.create(account.getUsername(), "title", "short", "long", "OPEN");
        Topic topic = topicFactory.create(magazine.getId(), account.getUsername(), "title", "short", "long");
        Article article = articleFactory.create(topic.getId(), account.getUsername(), "title", "content");

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
    void show_hasReply() throws Exception {
        Account account = accountFactory.create("scene", "scene@email.com", "password");
        Magazine magazine = magazineFactory.create(account.getUsername(), "title", "short", "long", "OPEN");
        Topic topic = topicFactory.create(magazine.getId(), account.getUsername(), "title", "short", "long");
        Article article = articleFactory.create(topic.getId(), account.getUsername(), "title", "content");
        replyService.save(article.getId(), account.getUsername(), "This is test reply.");

        mockMvc.perform(
                get("/articles/" + article.getId())
        )
                .andExpect(status().isOk())
                .andExpect(model().attribute("article", article))
        ;
        assertThat(article.getReplies()).anyMatch((r) -> r.getContent().equals("This is test reply."));
    }

}