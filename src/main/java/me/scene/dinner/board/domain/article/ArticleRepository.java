package me.scene.dinner.board.domain.article;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByWriterAndPublicizedOrderByRatingDesc(String writer, boolean publicized);

    List<Article> findByWriterAndPublicizedOrderByCreatedAtAsc(String writer, boolean publicized);

}
