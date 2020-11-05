//package me.scene.dinner.board.article;
//
//import me.scene.dinner.account.domain.AccountRepository;
//import me.scene.dinner.account.utils.WithAccount;
//import me.scene.dinner.board.article.domain.Article;
//import me.scene.dinner.board.article.domain.ArticleRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class ArticleControllerTest {
//
//    @Autowired MockMvc mockMvc;
//    @Autowired ArticleRepository articleRepository;
//    @Autowired AccountRepository accountRepository;
//
//    @BeforeEach
//    void beforeEach() {
//
//    }
//
//    @AfterEach
//    void clearRepositories() {
//        articleRepository.deleteAll();
//        accountRepository.deleteAll();
//    }
//
//    @Test
//    @WithAccount(username = "scene")
//    void articleCreatePage_hasForm() throws Exception {
//        mockMvc.perform(
//                get("/topic/1/article-form")
//        )
//                .andExpect(status().isOk())
//                .andExpect(view().name("page/board/article/form"))
//                .andExpect(model().attributeExists("articleForm"))
//        ;
//    }
//
//    @Test
//    void articleCreatePage_unauthenticated_beGuidedBySpringSecurity() throws Exception {
//        mockMvc.perform(
//                get("/topic/1/article-form")
//        )
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrlPattern("**/login"))
//        ;
//    }
//    // TODO
//    // void articleCreatePage_unauthorized() throws Exception {
//
//    @Test
//    @WithAccount(username = "scene")
//    @Transactional
//    void articleSubmit_save() throws Exception {
//        mockMvc.perform(
//                post("/articles")
//                        .with(csrf())
//                        .param("parentUrl", "/magazine/topic/")
//                        .param("title", "title")
//                        .param("content", "content")
//                        .param("url", "article")
//        )
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrlPattern("/articles/*"))
//        ;
//
//        Article article = articleRepository.findByTitle("title").orElseThrow();
//        assertThat(article).isNotNull();
//    }
//
//    @Test
//    @WithAccount(username = "scene")
//    void articleSubmit_invalidParam_notSave() throws Exception {
//        mockMvc.perform(
//                post("/articles")
//                        .with(csrf())
//                        .param("parentUrl", "/magazine/topic/")
//                        .param("title", "")
//                        .param("content", "")
//                        .param("url", "한국어")
//        )
//                .andExpect(status().isOk())
//                .andExpect(view().name("page/board/article/form"))
//                .andExpect(model().hasErrors())
//                .andExpect(model().errorCount(3))
//        ;
//
//        Article article = articleRepository.findByTitle("").orElse(null);
//        assertThat(article).isNull();
//    }
//
//    @Test
//    void articleSubmit_unauthenticated_beGuidedBySpringSecurity() throws Exception {
//        mockMvc.perform(
//                post("/articles")
//                        .with(csrf())
//                        .param("title", "valid")
//                        .param("content", "valid")
//        )
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrlPattern("**/login"))
//        ;
//
//        Article article = articleRepository.findByTitle("valid").orElse(null);
//        assertThat(article).isNull();
//    }
//
//    // TODO
//    // void articleSubmit_unauthorized() throws Exception {
//
//    @Test
//    void articleRead_showArticle() throws Exception {
////        articleFactory.create("article");
//
//        mockMvc.perform(
//                get("/articles/1")
//        )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(view().name("page/board/article/view"))
//                .andExpect(model().attributeExists("title"))
//        ;
//    }
//
//    @Test
//    void articleRead_nonExistent_handleException() throws Exception {
//        mockMvc.perform(
//                get("/articles/1")
//        )
//                .andExpect(status().isOk())
//                .andExpect(view().name("page/error/board_not_found"))
//        ;
//    }
//
//}
