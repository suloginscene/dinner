package me.scene.dinner.integration.utils;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.application.command.ArticleService;
import me.scene.dinner.board.article.application.command.request.ArticleCreateRequest;
import me.scene.dinner.board.article.domain.ArticleRepository;
import me.scene.dinner.integration.utils.aop.LogAround;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ArticleTestHelper {

    private final ArticleService service;

    private final ArticleRepository repository;


    @LogAround
    public Long createArticle(String username, Long topicId, String title, boolean publicized) {
        ArticleCreateRequest request = new ArticleCreateRequest(username, topicId, title, "content", publicized);
        return service.save(request);
    }

    @LogAround
    public void clearArticles() {
        repository.deleteAll();
    }

}