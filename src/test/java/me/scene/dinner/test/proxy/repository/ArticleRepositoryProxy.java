package me.scene.dinner.test.proxy.repository;

import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.article.domain.ArticleRepository;

import java.util.Optional;

public interface ArticleRepositoryProxy extends ArticleRepository {

    Optional<Article> findByTitle(String title);

}
