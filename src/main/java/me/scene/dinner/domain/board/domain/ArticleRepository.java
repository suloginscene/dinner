package me.scene.dinner.domain.board.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findByTitle(String title);

    Optional<Article> findByUrl(String url);

    boolean existsByUrl(String url);

}
