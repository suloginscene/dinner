package me.scene.dinner.domain.board.article;

import me.scene.dinner.domain.account.AccountController;
import me.scene.dinner.domain.account.AccountRepository;
import me.scene.dinner.domain.account.WithAccount;
import me.scene.dinner.domain.board.topic.TopicController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ArticleControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ArticleRepository articleRepository;
    @Autowired AccountRepository accountRepository;

    @BeforeEach
    void beforeEach() {

    }

    @AfterEach
    void clearRepositories() {
        articleRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    @WithAccount(username = "scene")
    void articleCreatePage_hasForm() throws Exception {
        mockMvc.perform(
                get(TopicController.ARTICLE_FORM)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/board/article/form"))
                .andExpect(model().attributeExists("articleForm"))
        ;
    }

    @Test
    void articleCreatePage_unauthenticated() throws Exception {
        mockMvc.perform(
                get(TopicController.ARTICLE_FORM)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**" + AccountController.URL_LOGIN))
        ;
    }
    // TODO
    // void articleCreatePage_unauthorized() throws Exception {

    @Test
    @WithAccount(username = "scene")
    void articleSubmit_save() throws Exception {
        mockMvc.perform(
                post(ArticleController.URL)
                        .with(csrf())
                        .param("title", "title")
                        .param("content", "content")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(ArticleController.URL + "/*"))
        ;

        Article article = articleRepository.findByTitle("title").orElseThrow();
        assertThat(article).isNotNull();
    }

    @Test
    @WithAccount(username = "scene")
    void articleSubmit_invalid_notSave() throws Exception {
        mockMvc.perform(
                post(ArticleController.URL)
                        .with(csrf())
                        .param("title", "a".repeat(31))
                        .param("content", "")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/board/article/form"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(2))
        ;

        Article article = articleRepository.findByTitle("title").orElse(null);
        assertThat(article).isNull();
    }

    // TODO
    // void articleCreatePage_unauthenticated() throws Exception {

    // TODO
    // void articleCreatePage_unauthorized() throws Exception {

}
