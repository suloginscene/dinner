package me.scene.dinner.test.proxy.repository;

import me.scene.dinner.board.domain.article.Article;
import me.scene.dinner.board.domain.article.ArticleRepository;

import java.util.Optional;

public interface ArticleRepositoryProxy extends ArticleRepository {

    Optional<Article> findByTitle(String title);

}
