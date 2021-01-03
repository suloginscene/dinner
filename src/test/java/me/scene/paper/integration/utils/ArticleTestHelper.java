package me.scene.paper.integration.utils;

import lombok.RequiredArgsConstructor;
import me.scene.paper.board.article.application.command.ArticleService;
import me.scene.paper.board.article.application.command.request.ArticleCreateRequest;
import me.scene.paper.board.article.domain.article.repository.ArticleRepository;
import me.scene.paper.integration.utils.aop.LogAround;
import org.springframework.stereotype.Component;

import java.util.HashSet;


@Component
@RequiredArgsConstructor
public class ArticleTestHelper {

    private final ArticleService service;

    private final ArticleRepository repository;


    @LogAround
    public Long createArticle(String username, Long topicId, String title, boolean publicized) {
        ArticleCreateRequest request = new ArticleCreateRequest(username, topicId, title, "content", publicized, new HashSet<>());
        return service.save(request);
    }

    @LogAround
    public void clearArticles() {
        repository.deleteAll();
    }

}
