package me.scene.dinner.board.article.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findByTitle(String title);

    List<Article> findByWriterAndStatus(String writer, Status status);

}
