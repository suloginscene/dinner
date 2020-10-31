package me.scene.dinner.domain.board.article;

import me.scene.dinner.domain.account.AccountController;
import me.scene.dinner.domain.account.AccountRepository;
import me.scene.dinner.domain.account.WithAccount;
import me.scene.dinner.domain.board.UrlUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ArticleControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ArticleRepository articleRepository;
    @Autowired AccountRepository accountRepository;
    @Autowired ArticleFactory articleFactory;

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
                get(UrlUtils.form("magazine", "topic"))
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/board/article/form"))
                .andExpect(model().attributeExists("articleForm"))
        ;
    }

    @Test
    void articleCreatePage_unauthenticated_beGuidedBySpringSecurity() throws Exception {
        mockMvc.perform(
                get(UrlUtils.form("magazine", "topic"))
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**" + AccountController.URL_LOGIN))
        ;
    }
    // TODO
    // void articleCreatePage_unauthorized() throws Exception {

    @Test
    @WithAccount(username = "scene")
    @Transactional
    void articleSubmit_save() throws Exception {
        mockMvc.perform(
                post(UrlUtils.post("magazine", "topic"))
                        .with(csrf())
                        .param("parentUrl", "/magazine/topic/")
                        .param("title", "title")
                        .param("content", "content")
                        .param("url", "article")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(UrlUtils.read("*", "*", "*")))
        ;

        Article article = articleRepository.findByTitle("title").orElseThrow();
        assertThat(article).isNotNull();
    }

    @Test
    @WithAccount(username = "scene")
    void articleSubmit_invalidParam_notSave() throws Exception {
        mockMvc.perform(
                post(UrlUtils.post("magazine", "topic"))
                        .with(csrf())
                        .param("parentUrl", "/magazine/topic/")
                        .param("title", "")
                        .param("content", "")
                        .param("url", "한국어")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/board/article/form"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(3))
        ;

        Article article = articleRepository.findByTitle("").orElse(null);
        assertThat(article).isNull();
    }

    @Test
    void articleSubmit_unauthenticated_beGuidedBySpringSecurity() throws Exception {
        mockMvc.perform(
                post(UrlUtils.post("magazine", "topic"))
                        .with(csrf())
                        .param("title", "valid")
                        .param("content", "valid")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**" + AccountController.URL_LOGIN))
        ;

        Article article = articleRepository.findByTitle("valid").orElse(null);
        assertThat(article).isNull();
    }

    // TODO
    // void articleSubmit_unauthorized() throws Exception {

    @Test
    void articleRead_showArticle() throws Exception {
        articleFactory.create("article");

        mockMvc.perform(
                get(UrlUtils.read("magazine", "topic", "article"))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("page/board/article/view"))
                .andExpect(model().attributeExists("title"))
        ;
    }

    @Test
    void articleRead_nonExistent_handleException() throws Exception {
        mockMvc.perform(
                get(UrlUtils.read("magazine", "topic", "article"))
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/error/board_not_found"))
        ;
    }

}
