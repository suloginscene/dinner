package me.scene.dinner.board.domain.article;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByWriterAndStatus(String writer, Status status);

}
